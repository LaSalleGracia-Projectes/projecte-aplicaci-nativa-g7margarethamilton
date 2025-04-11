package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import retrofit2.Response


class ApiRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    val apiInterface = ApiService.create()

    //AUTH
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun register(usuari: User) = apiInterface.register(usuari)
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun login(usuari: User) = apiInterface.login(usuari)
    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return ApiService.create().loginWithGoogle(mapOf("id_token" to idToken))
    }
    // LOGOUT
    suspend fun logoutApp(email: String, password: String?, googleId: String?) =
        apiInterface.logoutApp(
            buildMap {
                put("email", email)
                password?.let { put("password", it) }
                googleId?.let { put("google_id", it) }
            }
        )

    //SCHEDULE
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllSchedules(token: String,) =
        apiInterface.getAllSchedules("Bearer $token")

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSchedule(token: String, id: String) =
        apiInterface.getSchedule("Bearer $token", id)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun createSchedule(token: String, userId: String, title: String, isFavorite: Boolean, categoryId: Int) = 
        apiInterface.createSchedule(
            "Bearer $token",
            CreateScheduleRequest(userId, title, isFavorite, categoryId)
        )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSchedule(token: String, id: String, userId: String, title: String, isFavorite: Boolean, categoryId: Int) = 
        apiInterface.updateSchedule(
            "Bearer $token",
            id,
            UpdateScheduleRequest(userId, title, isFavorite, categoryId)
        )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteSchedule(token: String, id: String, userId: String) = 
        apiInterface.deleteSchedule("Bearer $token", id)

    //TASK
    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllTasks(token: String) =
        apiInterface.getAllTasks("Bearer $token")
}