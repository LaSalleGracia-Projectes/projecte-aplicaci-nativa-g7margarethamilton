package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ScheduleViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()
    
    // Estados de UI
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList<Schedule>())
    val schedules: StateFlow<List<Schedule>> = _schedules

    private val _userTasks = MutableStateFlow<List<Schedule_task>>(emptyList())
    val userTasks: StateFlow<List<Schedule_task>> = _userTasks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentSchedule = MutableStateFlow<Schedule?>(null)
    val currentSchedule: StateFlow<Schedule?> = _currentSchedule

    private val _currentScheduleTasks = MutableStateFlow<List<Schedule_task>>(emptyList())
    val currentScheduleTasks: StateFlow<List<Schedule_task>> = _currentScheduleTasks

    private val _filteredTasksByDay = MutableStateFlow<List<Schedule_task>>(emptyList())
    val filteredTasksByDay: StateFlow<List<Schedule_task>> = _filteredTasksByDay

    /**
     * Filtra las tareas por día de la semana para el horario actual.
     * @param dayOfWeek Día de la semana (1-7, donde 1 es Domingo)
     */
    fun filterTasksByDay(dayOfWeek: Int) {
        Log.d("ScheduleViewModel", "Filtering tasks for day: $dayOfWeek")
        updateFilteredTasks(dayOfWeek)
    }

    /**
     * Añade una nueva tarea al horario.
     * @param scheduleId ID del horario
     * @param title Título de la tarea
     * @param content Contenido/descripción de la tarea
     * @param startTime Hora de inicio (formato HH:mm)
     * @param endTime Hora de fin (formato HH:mm)
     * @param week_day Día de la semana (1-7)
     * @param categoryId ID de la categoría
     * @param email Email del usuario
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun addTaskToSchedule(
        scheduleId: String,
        title: String,
        content: String,
        startTime: String,
        endTime: String,
        week_day: Int,
        categoryId: Int,
        email: String
    ) {
        val token = userViewModel.token.value ?: return
        executeApiCall(
            apiCall = {
                repository.createTask(
                    token = token,
                    userId = email,
                    title = title,
                    content = content,
                    priority = 1,
                    startTime = startTime,
                    endTime = endTime,
                    week_day = week_day,
                    scheduleId = scheduleId.toInt(),
                    categoryId = categoryId
                )
            },
            onSuccess = {
                refreshTasksAndUpdateUI(week_day)
            }
        )
    }

    /**
     * Elimina una tarea del horario.
     * @param taskId ID de la tarea a eliminar
     */
    fun deleteScheduleTask(taskId: String) {
        val token = userViewModel.token.value ?: return
        val currentDay = getCurrentSelectedDay()
        
        executeApiCall(
            apiCall = { repository.deleteTask(token, taskId) },
            onSuccess = {
                refreshTasksAndUpdateUI(currentDay)
            }
        )
    }

    /**
     * Carga todos los horarios del usuario.
     * @param userId ID del usuario
     */
    fun loadSchedules() {
        val token = userViewModel.token.value ?: return
        Log.w("ScheduleViewModel", "Cannot load schedules: token is null")

        executeApiCall(
            apiCall = { repository.getAllSchedules(token) },
            onSuccess = { schedulesList ->
                _schedules.value = schedulesList
                if (_currentSchedule.value == null && schedulesList.isNotEmpty()) {
                    setCurrentSchedule(schedulesList.first())
                }
                getAllTasks()
            }
        )
    }

    /**
     * Establece el horario actual y actualiza las tareas relacionadas.
     * @param schedule Horario a establecer como actual
     */
    fun setCurrentSchedule(schedule: Schedule) {
        _currentSchedule.value = schedule
        Log.d("ScheduleViewModel", "Current schedule set to: '${schedule.title}', ID: ${schedule.id}")
        updateCurrentScheduleTasks()
    }

    // Funciones privadas de utilidad

    /**
     * Actualiza las tareas filtradas para un día específico.
     * @param dayOfWeek Día de la semana para filtrar
     */
    private fun updateFilteredTasks(dayOfWeek: Int) {
        val currentScheduleId = _currentSchedule.value?.id
        val filteredTasks = _userTasks.value.filter { task ->
            task.week_day == dayOfWeek && task.id_schedule == currentScheduleId
        }.sortedBy { it.start_time }
        
        _filteredTasksByDay.value = filteredTasks
        Log.d("ScheduleViewModel", "Updated filtered tasks for day $dayOfWeek: ${filteredTasks.size}")
    }

    /**
     * Obtiene el día actualmente seleccionado.
     * @return Día de la semana seleccionado o 0 si no hay selección
     */
    private fun getCurrentSelectedDay(): Int {
        return _filteredTasksByDay.value.firstOrNull()?.week_day ?: 0
    }

    /**
     * Actualiza las tareas del horario actual.
     */
    private fun updateCurrentScheduleTasks() {
        val currentScheduleId = _currentSchedule.value?.id
        val currentDay = getCurrentSelectedDay()
        
        if (currentScheduleId != null) {
            val tasksFromSchedule = _currentSchedule.value?.tasks
            _currentScheduleTasks.value = when {
                !tasksFromSchedule.isNullOrEmpty() -> tasksFromSchedule
                else -> _userTasks.value.filter { it.id_schedule == currentScheduleId }
            }
            
            if (currentDay > 0) {
                updateFilteredTasks(currentDay)
            }
        } else {
            _currentScheduleTasks.value = emptyList()
            _filteredTasksByDay.value = emptyList()
        }
    }

    /**
     * Refresca las tareas y actualiza la UI.
     * @param dayOfWeek Día de la semana para filtrar después de refrescar
     */
    private fun refreshTasksAndUpdateUI(dayOfWeek: Int) {
        getAllTasks()
        CoroutineScope(Dispatchers.Main).launch {
            kotlinx.coroutines.delay(100)
            updateFilteredTasks(dayOfWeek)
        }
    }

    /**
     * Ejecuta una llamada a la API con manejo de errores estándar.
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

    /**
     * Obtiene todas las tareas del usuario.
     */
    fun getAllTasks() {
        val token = userViewModel.token.value ?: return
        
        executeApiCall(
            apiCall = { repository.getAllTasks(token) },
            onSuccess = { tasksList ->
                _userTasks.value = tasksList
                updateCurrentScheduleTasks()
            }
        )
    }

//    fun createNewSchedule(title: String, email: String, categoryId: Int) {
//        val token = userViewModel.token.value ?: return
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                _isLoading.value = true
//                val response = repository.createSchedule(
//                    token = token,
//                    userId = email,
//                    title = title,
//                    isFavorite = false,
//                    categoryId = categoryId
//                )
//
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        // Recargar los schedules para obtener el nuevo
//                        loadSchedules()
//                        //setCurrentSchedule(response.body()!!)
//                        _error.value = null
//                    } else {
//                        _error.value = "Error al crear la agenda: ${response.message()}"
//                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    _error.value = "Error: ${e.message}"
//                }
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
    fun createNewSchedule(
        title: String,
        email: String,
        categoryId: Int
    ) {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = {
                repository.createSchedule(
                    token = token,
                    userId = email,
                    title = title,
                    isFavorite = false,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadSchedules()
                executeApiCall(
                    apiCall = { repository.getAllSchedules(token) },
                    onSuccess = { schedulesList ->
                        _schedules.value = schedulesList
                        setCurrentSchedule(schedulesList.first())
                    }
                )


            }
        )
    }

    /**
     * Elimina un horario y selecciona el último disponible.
     * @param scheduleId ID del horario a eliminar
     */
    fun deleteSchedule(scheduleId: String) {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.deleteSchedule(token, scheduleId) },
            onSuccess = {
                // Recargar los horarios
                executeApiCall(
                    apiCall = { repository.getAllSchedules(token) },
                    onSuccess = { schedulesList ->
                        _schedules.value = schedulesList
                        // Si hay horarios disponibles, seleccionar el último
                        if (schedulesList.isNotEmpty()) {
                            setCurrentSchedule(schedulesList.last())
                        } else {
                            // Si no hay horarios, limpiar el horario actual
                            _currentSchedule.value = null
                            _currentScheduleTasks.value = emptyList()
                            _filteredTasksByDay.value = emptyList()
                        }
                    }
                )
            }
        )
    }
} 