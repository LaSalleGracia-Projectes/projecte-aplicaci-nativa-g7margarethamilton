package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar_task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel para la gestión de calendarios y sus tareas asociadas.
 *
 * Este ViewModel maneja todas las operaciones relacionadas con calendarios, incluyendo:
 * - Gestión de calendarios (crear, cargar, eliminar)
 * - Gestión de tareas de calendario (crear, actualizar, eliminar)
 * - Manejo del estado de la UI relacionado con calendarios
 *
 * @property userViewModel ViewModel del usuario necesario para obtener el token de autenticación
 */
class CalendarViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()

    // Estados de UI
    /**
     * Lista de todos los calendarios disponibles
     */
    private val _calendars = MutableStateFlow<List<Calendar>>(emptyList())
    val calendars: StateFlow<List<Calendar>> = _calendars

    /**
     * Lista de todas las tareas de calendario
     */
    private val _calendarTasks = MutableStateFlow<List<Calendar_task>>(emptyList())
    val calendarTasks: StateFlow<List<Calendar_task>> = _calendarTasks

    /**
     * Calendario actualmente seleccionado
     */
    private val _currentCalendar = MutableStateFlow<Calendar?>(null)
    val currentCalendar: StateFlow<Calendar?> = _currentCalendar

    /**
     * Fecha actualmente seleccionada
     */
    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    /**
     * Estado de carga de operaciones
     */
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /**
     * Mensaje de error si ocurre algún problema
     */
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Establece la fecha seleccionada
     * @param date Nueva fecha a seleccionar
     */
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * Carga todos los calendarios disponibles del usuario
     * Si hay calendarios disponibles, establece el primero como calendario actual
     */
    fun loadCalendars() {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.getAllCalendars(token) },
            onSuccess = { calendarList ->
                _calendars.value = calendarList
                if (_currentCalendar.value == null && calendarList.isNotEmpty()) {
                    setCurrentCalendar(calendarList.first())
                }
                loadAllCalendarTasks()
            }
        )
    }

    /**
     * Establece el calendario actual
     * @param calendar Calendario a establecer como actual
     */
    fun setCurrentCalendar(calendar: Calendar) {
        _currentCalendar.value = calendar
        Log.d("CalendarViewModel", "Current calendar set to: ${calendar.title}")
    }

    /**
     * Crea un nuevo calendario
     * @param title Título del calendario
     * @param email Email del usuario propietario
     * @param categoryId ID de la categoría del calendario
     */
    fun createNewCalendar(
        title: String,
        email: String,
        categoryId: Int
    ) {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = {
                repository.createCalendar(
                    token = token,
                    userId = email,
                    title = title,
                    isFavorite = false,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadCalendars()
            }
        )
    }

    /**
     * Crea una nueva tarea en el calendario actual
     * @param title Título de la tarea
     * @param content Contenido/descripción de la tarea
     * @param startTime Hora de inicio (formato ISO)
     * @param endTime Hora de fin (formato ISO)
     * @param categoryId ID de la categoría de la tarea
     * @param email Email del usuario propietario
     */
    fun createCalendarTask(
        title: String,
        content: String,
        startTime: String,
        endTime: String,
        categoryId: Int,
        email: String
    ) {
        val token = userViewModel.token.value ?: return
        val currentCalendarId = _currentCalendar.value?.id ?: return

        // Formato para parsear y formatear las fechas
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parsear las fechas a LocalDateTime
        val startDateTime = LocalDateTime.parse(startTime, formatter)
        val endDateTime = LocalDateTime.parse(endTime, formatter)

        // Sumar 4 horas a cada tiempo
        val adjustedStartTime = startDateTime.plusHours(4)
        val adjustedEndTime = endDateTime.plusHours(4)

        // Convertir de nuevo a String con el formato requerido
        val adjustedStartTimeStr = adjustedStartTime.format(formatter)
        val adjustedEndTimeStr = adjustedEndTime.format(formatter)

        println(endTime)

        executeApiCall(
            apiCall = {
                repository.createCalendarTask(
                    token = token,
                    userId = email,
                    title = title,
                    content = content,
                    isCompleted = false,
                    priority = 1,
                    startTime = adjustedStartTimeStr,
                    endTime = adjustedEndTimeStr,
                    calendarId = currentCalendarId,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    /**
     * Elimina una tarea del calendario actual
     * @param taskId ID de la tarea a eliminar
     */
    fun deleteCalendarTask(taskId: String) {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.deleteCalendarTask(token, taskId) },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    /**
     * Carga todas las tareas de calendario disponibles
     */
    private fun loadAllCalendarTasks() {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.getAllCalendarTask(token) },
            onSuccess = { tasksList ->
                _calendarTasks.value = tasksList
            }
        )
    }

    /**
     * Actualiza una tarea existente en el calendario
     * @param taskId ID de la tarea a actualizar
     * @param title Nuevo título
     * @param content Nuevo contenido
     * @param isCompleted Nuevo estado de completado
     * @param startTime Nueva hora de inicio
     * @param endTime Nueva hora de fin
     * @param categoryId Nueva categoría
     * @param email Email del usuario
     */
    fun updateCalendarTask(
        taskId: Int,
        title: String,
        content: String,
        isCompleted: Boolean,
        startTime: String,
        endTime: String,
        categoryId: Int,
        email: String
    ) {

        val token = userViewModel.token.value ?: return
        // Se obtiene el ID del calendario actual
        val currentCalendarId = _currentCalendar.value?.id ?: return

        // Formato para parsear y formatear las fechas
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parsear las fechas a LocalDateTime
        val startDateTime = LocalDateTime.parse(startTime, formatter)
        val endDateTime = LocalDateTime.parse(endTime, formatter)

        // Sumar 4 horas a cada tiempo
        val adjustedStartTime = startDateTime.plusHours(4)
        val adjustedEndTime = endDateTime.plusHours(4)

        // Convertir de nuevo a String con el formato requerido
        val adjustedStartTimeStr = adjustedStartTime.format(formatter)
        val adjustedEndTimeStr = adjustedEndTime.format(formatter)


        executeApiCall(
            apiCall = {
                repository.updateCalendarTask(
                    token = token,
                    id = taskId.toString(),
                    userId = email,
                    title = title,
                    content = content,
                    isCompleted = isCompleted,
                    priority = 1,
                    startTime = adjustedStartTimeStr,
                    endTime = adjustedEndTimeStr,
                    calendarId = currentCalendarId,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    /**
     * Función de utilidad para ejecutar llamadas a la API de forma segura
     * @param apiCall Lambda con la llamada a la API
     * @param onSuccess Lambda a ejecutar en caso de éxito
     */
    private fun <T> executeApiCall(
        apiCall: suspend () -> retrofit2.Response<T>,
        onSuccess: (T) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = apiCall()

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { onSuccess(it) }
                        _error.value = null
                    } else {
                        _error.value = "Error en la operación: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _error.value = "Error: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}