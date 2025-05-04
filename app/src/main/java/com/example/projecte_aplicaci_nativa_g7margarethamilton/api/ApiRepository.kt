package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import retrofit2.Response


class ApiRepository {
    private val apiInterface = ApiService.create()

    //AUTH
    suspend fun register(usuari: User) = apiInterface.register(usuari)
    suspend fun login(usuari: User) = apiInterface.login(usuari)
    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return ApiService.create().loginWithGoogle(mapOf("id_token" to idToken))
    }

    suspend fun resetPassword(email: String) =
        apiInterface.resetPassword(mapOf("email" to email))

    // LOGOUT
    suspend fun logoutApp(email: String, password: String?, googleId: String?) =
        apiInterface.logoutApp(
            buildMap {
                put("email", email)
                password?.let { put("password", it) }
                googleId?.let { put("google_id", it) }
            }
        )

    suspend fun getUser(token: String, email: String) =
        apiInterface.getUser(
            "Bearer $token",
            email
        )

    suspend fun updateUser(
        token: String,
        email: String,
        nickname: String?,
        avatarUrl: String?,
        password: Any?,
        isAdmin: Boolean,
        isBanned: Boolean,
    ) = apiInterface.updateUser(
        "Bearer $token",
        email,
        UpdateUserRequest(nickname, avatarUrl, password, isAdmin, isBanned)
    )

    suspend fun deleteUser(token: String, email: String) =
        apiInterface.deleteUser(
            "Bearer $token",
            email
        )

    suspend fun getUserSettings(
        token: String,
        email: String
    ) = apiInterface.getUserSettings("Bearer $token", email)

    suspend fun updateUserSettings(
        token: String,
        email: String,
        themeMode: Boolean,
        langCode: String,
        allowNotification: Boolean,
        mergeScheduleCalendar: Boolean
    ) = apiInterface.updateUserSettings(
        "Bearer $token",
        email,
        UpdateSettingsRequest(
            themeMode,
            langCode,
            allowNotification,
            mergeScheduleCalendar
        )
    )
    suspend fun contactUs(
        email: String,
        message: String
    ) = apiInterface.contactUs(
        ContactRequest(
            email = email,
            message = message
        )
    )

    //SCHEDULE
    suspend fun getAllSchedules(token: String) =
        apiInterface.getAllSchedules("Bearer $token")

    suspend fun getSchedule(token: String, id: String) =
        apiInterface.getSchedule("Bearer $token", id)


    suspend fun createSchedule(
        token: String,
        userId: String,
        title: String,
        isFavorite: Boolean,
        categoryId: Int
    ) =
        apiInterface.createSchedule(
            "Bearer $token",
            CreateScheduleRequest(userId, title, isFavorite, categoryId)
        )


    suspend fun updateSchedule(
        token: String,
        id: String,
        userId: String,
        title: String,
        isFavorite: Boolean,
        categoryId: Int
    ) =
        apiInterface.updateSchedule(
            "Bearer $token",
            id,
            UpdateScheduleRequest(userId, title, isFavorite, categoryId)
        )

    suspend fun deleteSchedule(token: String, id: String) =

        apiInterface.deleteSchedule("Bearer $token", id)

    //SCHEDULE-TASK
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
        CreateTaskRequest(
            userId = userId,
            title = title,
            content = content,
            priority = priority,
            start_time = startTime,
            end_time = endTime,
            week_day = week_day,
            id_schedule = scheduleId,
            id_category = categoryId
        )
    )

    suspend fun deleteTask(token: String, id: String) =
        apiInterface.deleteTask("Bearer $token", id)

    suspend fun getAllTasks(token: String) =
        apiInterface.getAllTasks("Bearer $token")

    /**
     * CALENDAR
     */

    suspend fun getAllCalendars(token: String) =
        apiInterface.getAllCalendar("Bearer $token")

    suspend fun createCalendar(
        token: String,
        userId: String,
        title: String,
        isFavorite: Boolean,
        categoryId: Int
    ) =
        apiInterface.createCalendar(
            "bearer $token",
            CreateCalendarRequest(
                userId = userId,
                title = title,
                is_favorite = isFavorite,
                id_category = categoryId
            )
        )

    suspend fun getCalendarTask(
        token: String,
        id: String
    ) =
        apiInterface.getCalendarTask("Bearer $token", id)

    suspend fun getAllCalendarTask(token: String) =
        apiInterface.getAllCalendarTask("Bearer $token")

    suspend fun createCalendarTask(
        token: String,
        userId: String,
        title: String,
        content: String,
        isCompleted: Boolean,
        priority: Int,
        startTime: String,
        endTime: String,
        calendarId: Int,
        categoryId: Int
    ) = apiInterface.createCalendarTask(
        "Bearer $token",
        CreateCalendarTaskRequest(
            userId = userId,
            title = title,
            content = content,
            is_completed = isCompleted,
            priority = priority,
            start_time = startTime,
            end_time = endTime,
            id_calendar = calendarId,
            id_category = categoryId
        )
    )

    suspend fun updateCalendarTask(
        token: String,
        id: String,
        userId: String,
        title: String,
        content: String,
        isCompleted: Boolean,
        priority: Int,
        startTime: String,
        endTime: String,
        calendarId: Int,
        categoryId: Int
    ) = apiInterface.updateCalendarTask(
        "Bearer $token",
        id,
        CreateCalendarTaskRequest(
            userId = userId,
            title = title,
            content = content,
            is_completed = isCompleted,
            priority = priority,
            start_time = startTime,
            end_time = endTime,
            id_calendar = calendarId,
            id_category = categoryId
        )
    )

    suspend fun deleteCalendarTask(token: String, id: String) =
        apiInterface.deleteCalendarTask("Bearer $token", id)



}