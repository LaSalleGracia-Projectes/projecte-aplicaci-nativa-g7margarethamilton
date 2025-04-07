package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import androidx.lifecycle.ViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScheduleViewModel(
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val repository = ApiRepository()
    
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _currentSchedule = MutableStateFlow<Schedule?>(null)
    val currentSchedule: StateFlow<Schedule?> = _currentSchedule

    fun loadSchedules(userId: String) {
        val token = userViewModel.token.value ?: return
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = repository.getAllSchedules(token)
                
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        _schedules.value = response.body() ?: emptyList()
                        _error.value = null
                    } else {
                        _error.value = "Error al cargar los horarios: ${response.message()}"
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