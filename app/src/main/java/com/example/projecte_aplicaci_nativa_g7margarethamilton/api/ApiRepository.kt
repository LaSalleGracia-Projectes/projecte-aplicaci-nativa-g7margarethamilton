package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import retrofit2.Response


class ApiRepository {
    val apiInterface = ApiService.create()

    //AUTH
    suspend fun register(usuari: User) = apiInterface.register(usuari)
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
        week_day: Int,
        scheduleId: Int,
        categoryId: Int
    ) = apiInterface.createTask(
        "Bearer $token",
        CreateTaskRequest(userId = userId, title =  title, content =  content, priority =  priority, start_time =  startTime, end_time =  endTime, week_day =  week_day, id_schedule =  scheduleId, id_category =  categoryId)
    )

    suspend fun deleteTask(token: String, id: String) =
        apiInterface.deleteTask("Bearer $token", id)

    suspend fun getAllTasks(token: String) =
        apiInterface.getAllTasks("Bearer $token")
}