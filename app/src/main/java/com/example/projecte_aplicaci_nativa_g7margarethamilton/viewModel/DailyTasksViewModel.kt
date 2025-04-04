package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import androidx.lifecycle.ViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DailyTasksViewModel : ViewModel() {
    private val repository = ApiRepository()
    
    private val _schedules = MutableStateFlow<List<Schedule>>(emptyList())
    val schedules: StateFlow<List<Schedule>> = _schedules
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSchedules(userId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                _isLoading.value = true
                val response = repository.getAllSchedules(userId)
                
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

    fun updateTaskCompletion(scheduleId: String, taskId: Int, isCompleted: Boolean) {
        // Obtener el schedule actual
        val currentSchedule = _schedules.value.find { it.id.toString() == scheduleId }
        
        if (currentSchedule != null) {
            // Crear una copia actualizada del schedule
            val updatedSchedule = currentSchedule.copy(
                tasks = currentSchedule.tasks.map { task ->
                    if (task.id == taskId) {
                        task.copy(is_completed = isCompleted)
                    } else {
                        task
                    }
                }
            )
            
            // Actualizar en la API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = repository.updateSchedule(scheduleId, updatedSchedule)
                    if (!response.isSuccessful) {
                        _error.value = "Error al actualizar la tarea: ${response.message()}"
                    }
                } catch (e: Exception) {
                    _error.value = "Error: ${e.message}"
                }
            }
        }
    }
} 