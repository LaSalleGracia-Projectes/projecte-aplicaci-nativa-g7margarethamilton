package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.UserSettings
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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getUser(token: String, email: String) =
        apiInterface.getUser(
            "Bearer $token",
            email
        )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateUser(
        token: String,
        email: String,
        nickname: String?,
        avatarUrl: String?,
        isAdmin: Boolean,
        isBanned: Boolean
    ) = apiInterface.updateUser(
        "Bearer $token",
        email,
        UpdateUserRequest(nickname, avatarUrl, isAdmin, isBanned)
    )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteUser(token: String, email: String) =
        apiInterface.deleteUser(
            "Bearer $token",
            email
        )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getSettings(token: String, email: String): Response<UserSettings> =
        apiInterface.getSettings("Bearer $token", email)

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateSettings(
        token: String,
        email: String,
        themeMode: Boolean,
        langCode: String,
        allowNotifications: Boolean,
        mergeScheduleCalendar: Boolean
    ): Response<UserSettings> =
        apiInterface.updateSettings(
            "Bearer $token",
            email,
            UpdateSettingsRequest(
                theme_mode = themeMode,
                lang_code = langCode,
                allow_notifications = allowNotifications,
                merge_schedule_calendar = mergeScheduleCalendar
            )
        )

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun deleteSettings(token: String, email: String): Response<Unit> =
        apiInterface.deleteSettings("Bearer $token", email)

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

    data class UpdateUserRequest(
        val nickname: String?,
        val avatar_url: String?,
        val is_admin: Boolean,
        val is_banned: Boolean
    )

    data class UpdateSettingsRequest(
        val theme_mode: Boolean,
        val lang_code: String,
        val allow_notifications: Boolean,
        val merge_schedule_calendar: Boolean
    )
}