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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class ScheduleViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()
    
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

    // Nuevo StateFlow para las tareas filtradas del schedule actual
    private val _currentScheduleTasks = MutableStateFlow<List<Schedule_task>>(emptyList())
    val currentScheduleTasks: StateFlow<List<Schedule_task>> = _currentScheduleTasks

    // Nuevo StateFlow para las tareas filtradas por día
    private val _filteredTasksByDay = MutableStateFlow<List<Schedule_task>>(emptyList())
    val filteredTasksByDay: StateFlow<List<Schedule_task>> = _filteredTasksByDay

    // Función para filtrar tareas por día de la semana
    fun filterTasksByDay(dayOfWeek: Int) {
        Log.d("ScheduleViewModel", "Filtering tasks for day: $dayOfWeek")
        
        // Obtener el ID del schedule actual
        val currentScheduleId = _currentSchedule.value?.id
        
        Log.d("ScheduleViewModel", "Current Schedule ID: $currentScheduleId")
        Log.d("ScheduleViewModel", "Total tasks: ${_userTasks.value.size}")
        
        // Filtrar tareas por día de la semana y schedule actual
        val filteredTasks = _userTasks.value.filter { task ->
            task.week_day == dayOfWeek && task.id_schedule == currentScheduleId
        }.sortedBy { it.start_time }
        
        Log.d("ScheduleViewModel", "Filtered tasks for day $dayOfWeek: ${filteredTasks.size}")
        _filteredTasksByDay.value = filteredTasks
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadSchedules(userId: String) {
        val token = userViewModel.token.value
        if (token == null) {
            Log.w("ScheduleViewModel", "Cannot load schedules: token is null")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                Log.d("ScheduleViewModel", "Fetching schedules from API for user: $userId")
                val response = repository.getAllSchedules(token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val schedulesList = response.body()
                        if (schedulesList != null) {
                            Log.d("ScheduleViewModel", "Received ${schedulesList.size} schedules from API")
                        }


                        if (schedulesList != null) {
                            _schedules.value = schedulesList
                        }

                        // Set first schedule as current if available and none is selected
                        if (_currentSchedule.value == null && schedulesList?.isNotEmpty() == true) {
                            schedulesList?.let { setCurrentSchedule(it.first()) }
                            Log.d("ScheduleViewModel", "Auto-selected first schedule: '${schedulesList.first().title}'")
                        }

                        // También cargamos todas las tareas
                        getAllTasks()

                        _error.value = null
                    } else {
                        Log.e("ScheduleViewModel", "API error: ${response.code()} - ${response.message()}")
                        _error.value = "Error al cargar los horarios: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Exception loading schedules", e)
                withContext(Dispatchers.Main) {
                    _error.value = "Error: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setCurrentSchedule(schedule: Schedule) {
        _currentSchedule.value = schedule
        Log.d("ScheduleViewModel", "Current schedule set to: '${schedule.title}', ID: ${schedule.id}")

        // Actualizar las tareas filtradas cuando cambia el schedule actual
        updateCurrentScheduleTasks()

        // Log tasks info
        val tasksInSchedule = schedule.tasks
        if (tasksInSchedule != null) {
            Log.d("ScheduleViewModel", "Tasks in current schedule object: ${tasksInSchedule.size}")
            tasksInSchedule.forEach { task ->
                Log.d("ScheduleViewModel", "Task in schedule: ${task.title} (${task.start_time}-${task.end_time})")
            }
        } else {
            Log.d("ScheduleViewModel", "Schedule object has null tasks list")
            // Filtrar tareas de userTasks
            val filteredTasks = _userTasks.value.filter { it.id_schedule == schedule.id }
            Log.d("ScheduleViewModel", "Filtered tasks from userTasks: ${filteredTasks.size}")
        }
    }

    // Nueva función para actualizar las tareas del schedule actual
    private fun updateCurrentScheduleTasks() {
        val currentScheduleId = _currentSchedule.value?.id
        val currentDay = _filteredTasksByDay.value.firstOrNull()?.week_day
        
        if (currentScheduleId != null) {
            // Primero intentamos obtener las tareas del objeto Schedule
            val tasksFromSchedule = _currentSchedule.value?.tasks
            
            if (tasksFromSchedule != null && tasksFromSchedule.isNotEmpty()) {
                _currentScheduleTasks.value = tasksFromSchedule
                Log.d("ScheduleViewModel", "Using tasks from schedule object: ${tasksFromSchedule.size}")
            } else {
                // Si no hay tareas en el objeto Schedule, filtramos de userTasks
                val filteredTasks = _userTasks.value.filter { it.id_schedule == currentScheduleId }
                _currentScheduleTasks.value = filteredTasks
                Log.d("ScheduleViewModel", "Using filtered tasks from userTasks: ${filteredTasks.size}")
            }

            // Si hay un día seleccionado, actualizar las tareas filtradas
            currentDay?.let { day ->
                updateFilteredTasks(day)
            }
        } else {
            _currentScheduleTasks.value = emptyList()
            _filteredTasksByDay.value = emptyList()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNewSchedule(title: String, email: String, categoryId: Int) {
        val token = userViewModel.token.value ?: return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = repository.createSchedule(
                    token = token,
                    userId = email,
                    title = title,
                    isFavorite = false,
                    categoryId = categoryId
                )
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Recargar los schedules para obtener el nuevo
                        loadSchedules(email)
                        _error.value = null
                    } else {
                        _error.value = "Error al crear la agenda: ${response.message()}"
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllTasks() {
        val token = userViewModel.token.value ?: return
        
        // Guardar el día actual seleccionado antes de recargar
        val currentDay = _filteredTasksByDay.value.firstOrNull()?.week_day

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                Log.d("ScheduleViewModel", "Fetching all tasks from API")
                val response = repository.getAllTasks(token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val tasksList = response.body()
                        _userTasks.value = tasksList ?: emptyList()
                        Log.d("ScheduleViewModel", "Received ${tasksList?.size} tasks from API")

                        // Actualizar las tareas del schedule actual
                        updateCurrentScheduleTasks()
                        
                        // Si teníamos un día seleccionado, actualizar las tareas filtradas
                        currentDay?.let { day ->
                            updateFilteredTasks(day)
                        }

                        _error.value = null
                    } else {
                        Log.e("ScheduleViewModel", "API error loading tasks: ${response.code()} - ${response.message()}")
                        _error.value = "Error al cargar las tareas: ${response.message()}"
                    }
                }
            } catch (e: Exception) {
                Log.e("ScheduleViewModel", "Exception loading tasks", e)
                withContext(Dispatchers.Main) {
                    _error.value = "Error: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = repository.createTask(
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
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Recargar todas las tareas después de crear una nueva
                        getAllTasks()
                        
                        // Esperar un momento para asegurar que las tareas se han cargado
                        kotlinx.coroutines.delay(100)
                        
                        // Actualizar las tareas filtradas con el día seleccionado
                        updateFilteredTasks(week_day)
                        
                        _error.value = null
                    } else {
                        _error.value = "Error al añadir la tarea: ${response.message()}"
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

    fun deleteScheduleTask(taskId: String) {
        val token = userViewModel.token.value ?: return
        
        // Obtener el día actual de las tareas filtradas antes de eliminar
        val currentFilteredDay = _filteredTasksByDay.value.firstOrNull()?.week_day ?: 0

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = repository.deleteTask(token, taskId)

                withContext(Dispatchers.Main) {
                    // Consideramos exitosa la eliminación si el código es 200 o 404
                    if (response.isSuccessful || response.code() == 404) {
                        // Eliminar la tarea del estado local
                        val updatedTasks = _userTasks.value.toMutableList()
                        updatedTasks.removeAll { it.id.toString() == taskId }
                        _userTasks.value = updatedTasks

                        // Actualizar las tareas filtradas
                        updateFilteredTasks(currentFilteredDay)

                        _error.value = null
                    } else {
                        _error.value = "Error al eliminar la tarea: ${response.message()}"
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

    // Nueva función para actualizar las tareas filtradas
    private fun updateFilteredTasks(dayOfWeek: Int) {
        Log.d("ScheduleViewModel", "Updating filtered tasks for day: $dayOfWeek")
        
        // Obtener el ID del schedule actual
        val currentScheduleId = _currentSchedule.value?.id
        
        Log.d("ScheduleViewModel", "Current Schedule ID: $currentScheduleId")
        Log.d("ScheduleViewModel", "Total tasks: ${_userTasks.value.size}")
        
        // Filtrar tareas por día de la semana y schedule actual
        val filteredTasks = _userTasks.value.filter { task ->
            task.week_day == dayOfWeek && task.id_schedule == currentScheduleId
        }.sortedBy { it.start_time }
        
        Log.d("ScheduleViewModel", "Filtered tasks for day $dayOfWeek: ${filteredTasks.size}")
        _filteredTasksByDay.value = filteredTasks
    }
} 