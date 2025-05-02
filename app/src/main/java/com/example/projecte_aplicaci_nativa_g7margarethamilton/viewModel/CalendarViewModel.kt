package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import java.time.LocalTime
import java.sql.Time

class CalendarViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()

    // Estados de UI
    private val _calendars = MutableStateFlow<List<Calendar>>(emptyList())
    val calendars: StateFlow<List<Calendar>> = _calendars

    private val _calendarTasks = MutableStateFlow<List<Calendar_task>>(emptyList())
    val calendarTasks: StateFlow<List<Calendar_task>> = _calendarTasks

    private val _currentCalendar = MutableStateFlow<Calendar?>(null)
    val currentCalendar: StateFlow<Calendar?> = _currentCalendar

    private val _selectedDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Funciones públicas
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    fun setCurrentCalendar(calendar: Calendar) {
        _currentCalendar.value = calendar
        Log.d("CalendarViewModel", "Current calendar set to: ${calendar.title}")
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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


        executeApiCall(
            apiCall = {
                repository.createCalendarTask(
                    token = token,
                    userId = email,
                    title = title,
                    content = content,
                    isCompleted = false,
                    priority = 1,
                    startTime = startTime.toString(),
                    endTime = endTime.toString(),
                    calendarId = currentCalendarId,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    fun deleteCalendarTask(taskId: String) {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.deleteCalendarTask(token, taskId) },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    private fun loadAllCalendarTasks() {
        val token = userViewModel.token.value ?: return

        executeApiCall(
            apiCall = { repository.getAllCalendarTask(token) },
            onSuccess = { tasksList ->
                _calendarTasks.value = tasksList
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        val currentCalendarId = _currentCalendar.value?.id ?: return

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
                    startTime = startTime,
                    endTime = endTime,
                    calendarId = currentCalendarId,
                    categoryId = categoryId
                )
            },
            onSuccess = {
                loadAllCalendarTasks()
            }
        )
    }

    // Función de utilidad para ejecutar llamadas a la API
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