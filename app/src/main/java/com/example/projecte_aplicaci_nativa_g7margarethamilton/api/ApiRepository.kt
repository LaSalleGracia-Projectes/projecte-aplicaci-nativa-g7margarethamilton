package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import retrofit2.Response


class ApiRepository {
    val apiInterface = ApiService.create()

    //AUTH
    suspend fun register(user: User) = apiInterface.register(user)
    suspend fun login(user: User) = apiInterface.login(user)
    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return ApiService.create().loginWithGoogle(mapOf("id_token" to idToken))
    }

    //SCHEDULE
    suspend fun getAllSchedules(token: String,) =
        apiInterface.getAllSchedules("Bearer $token")
    
    suspend fun getSchedule(token: String, id: String) =
        apiInterface.getSchedule("Bearer $token", id)
    
    suspend fun createSchedule(token: String, userId: String, title: String, isFavorite: Boolean, categoryId: Int) = 
        apiInterface.createSchedule(
            "Bearer $token",
            CreateScheduleRequest(userId, title, isFavorite, categoryId)
        )
    
    suspend fun updateSchedule(token: String, id: String, userId: String, title: String, isFavorite: Boolean, categoryId: Int) = 
        apiInterface.updateSchedule(
            "Bearer $token",
            id,
            UpdateScheduleRequest(userId, title, isFavorite, categoryId)
        )
    
    suspend fun deleteSchedule(token: String, id: String, userId: String) = 
        apiInterface.deleteSchedule("Bearer $token", id)

    //TASK
    suspend fun createTask(
        token: String,
        userId: String,
        title: String,
        content: String,
        priority: Int,
        startTime: String,
        endTime: String,
        scheduleId: Int,
        categoryId: Int
    ) = apiInterface.createTask(
        "Bearer $token",
        CreateTaskRequest(userId, title, content, priority, startTime, endTime, scheduleId, categoryId)
    )
}