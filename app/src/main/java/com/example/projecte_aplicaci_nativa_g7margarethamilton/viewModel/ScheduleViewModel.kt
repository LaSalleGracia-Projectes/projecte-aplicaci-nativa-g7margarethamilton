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

class ScheduleViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()
    
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
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
                        val schedulesList = response.body() ?: emptyList()
                        Log.d("ScheduleViewModel", "Received ${schedulesList.size} schedules from API")

                        // Log details of each schedule
                        schedulesList.forEachIndexed { index, schedule ->
                            Log.d("ScheduleViewModel", "Schedule $index: '${schedule.title}', ID: ${schedule.id}, Tasks: ${schedule.tasks?.size ?: 0}")
                        }

                        _schedules.value = schedulesList

                        // Set first schedule as current if available and none is selected
                        if (_currentSchedule.value == null && schedulesList.isNotEmpty()) {
                            setCurrentSchedule(schedulesList.first())
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
        } else {
            _currentScheduleTasks.value = emptyList()
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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                Log.d("ScheduleViewModel", "Fetching all tasks from API")
                val response = repository.getAllTasks(token)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val tasksList = response.body() ?: emptyList()
                        _userTasks.value = tasksList
                        Log.d("ScheduleViewModel", "Received ${tasksList.size} tasks from API")

                        // Log some task details
                        tasksList.groupBy { it.id_schedule }.forEach { (scheduleId, tasks) ->
                            Log.d("ScheduleViewModel", "Schedule ID $scheduleId has ${tasks.size} tasks")
                        }

                        // Actualizar las tareas filtradas después de cargar todas las tareas
                        updateCurrentScheduleTasks()

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
        priority: Int = 1,
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
                    priority = priority,
                    startTime = startTime,
                    endTime = endTime,
                    scheduleId = scheduleId.toInt(),
                    categoryId = categoryId
                )
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Recargar los schedules para obtener la tarea nueva
                        loadSchedules(email)
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


} 