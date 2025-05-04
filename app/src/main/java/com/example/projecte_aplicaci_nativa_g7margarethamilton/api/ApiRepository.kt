package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import retrofit2.Response

/**
 * Repositorio que actúa como capa intermedia entre los ViewModels y la API.
 * 
 * Esta clase proporciona métodos para realizar todas las operaciones de red necesarias,
 * encapsulando la lógica de comunicación con el servidor y proporcionando una interfaz
 * limpia para los ViewModels.
 */
class ApiRepository {
    private val apiInterface = ApiService.create()

    /**
     * Operaciones de Autenticación
     */

    /**
     * Registra un nuevo usuario
     * @param usuari Datos del usuario a registrar
     * @return Respuesta del servidor con el resultado del registro
     */
    suspend fun register(usuari: User) = apiInterface.register(usuari)

    /**
     * Inicia sesión de usuario
     * @param usuari Credenciales del usuario
     * @return Respuesta del servidor con token y datos del usuario
     */
    suspend fun login(usuari: User) = apiInterface.login(usuari)

    /**
     * Inicia sesión con Google
     * @param idToken Token de identificación de Google
     * @return Respuesta del servidor con token y datos del usuario
     */
    suspend fun loginWithGoogle(idToken: String): Response<LoginResponse> {
        return ApiService.create().loginWithGoogle(mapOf("id_token" to idToken))
    }

    /**
     * Envía solicitud de restablecimiento de contraseña
     * @param email Email del usuario
     * @return Respuesta del servidor
     */
    suspend fun resetPassword(email: String) =
        apiInterface.resetPassword(mapOf("email" to email))

    /**
     * Operaciones de Usuario
     */

    /**
     * Cierra la sesión del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario (opcional)
     * @param googleId ID de Google (opcional)
     * @return Respuesta del servidor
     */
    suspend fun logoutApp(email: String, password: String?, googleId: String?) =
        apiInterface.logoutApp(
            buildMap {
                put("email", email)
                password?.let { put("password", it) }
                googleId?.let { put("google_id", it) }
            }
        )

    /**
     * Obtiene los datos de un usuario
     * @param token Token de autenticación
     * @param email Email del usuario
     * @return Datos del usuario
     */
    suspend fun getUser(token: String, email: String) =
        apiInterface.getUser(
            "Bearer $token",
            email
        )

    /**
     * Actualiza los datos de un usuario
     * @param token Token de autenticación
     * @param email Email del usuario
     * @param nickname Nuevo nombre de usuario (opcional)
     * @param avatarUrl Nueva URL de avatar (opcional)
     * @param password Nueva contraseña (opcional)
     * @param isAdmin Flag de administrador
     * @param isBanned Flag de usuario baneado
     * @return Respuesta con los datos actualizados
     */
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

    /**
     * Elimina un usuario
     * @param token Token de autenticación
     * @param email Email del usuario a eliminar
     * @return Respuesta del servidor
     */
    suspend fun deleteUser(token: String, email: String) =
        apiInterface.deleteUser(
            "Bearer $token",
            email
        )

    /**
     * Obtiene las configuraciones de un usuario
     * @param token Token de autenticación
     * @param email Email del usuario
     * @return Configuraciones del usuario
     */
    suspend fun getUserSettings(
        token: String,
        email: String
    ) = apiInterface.getUserSettings("Bearer $token", email)

    /**
     * Actualiza las configuraciones de un usuario
     * @param token Token de autenticación
     * @param email Email del usuario
     * @param themeMode Modo oscuro/claro
     * @param langCode Código de idioma
     * @param allowNotification Permitir notificaciones
     * @param mergeScheduleCalendar Fusionar calendario y horario
     * @return Respuesta con las configuraciones actualizadas
     */
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

    /**
     * Envía un mensaje de contacto
     * @param email Email del usuario
     * @param message Mensaje a enviar
     * @return Respuesta del servidor
     */
    suspend fun contactUs(
        email: String,
        message: String
    ) = apiInterface.contactUs(
        ContactRequest(
            email = email,
            message = message
        )
    )

    /**
     * Operaciones de Horarios
     */

    /**
     * Obtiene todos los horarios
     * @param token Token de autenticación
     * @return Lista de horarios
     */
    suspend fun getAllSchedules(token: String) =
        apiInterface.getAllSchedules("Bearer $token")

    /**
     * Obtiene un horario específico
     * @param token Token de autenticación
     * @param id ID del horario
     * @return Horario solicitado
     */
    suspend fun getSchedule(token: String, id: String) =
        apiInterface.getSchedule("Bearer $token", id)

    /**
     * Crea un nuevo horario
     * @param token Token de autenticación
     * @param userId ID del usuario
     * @param title Título del horario
     * @param isFavorite Si es favorito
     * @param categoryId ID de la categoría
     * @return Horario creado
     */
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

    /**
     * Actualiza un horario existente
     * @param token Token de autenticación
     * @param id ID del horario
     * @param userId ID del usuario
     * @param title Nuevo título
     * @param isFavorite Si es favorito
     * @param categoryId ID de la categoría
     * @return Horario actualizado
     */
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

    /**
     * Elimina un horario
     * @param token Token de autenticación
     * @param id ID del horario a eliminar
     * @return Respuesta del servidor
     */
    suspend fun deleteSchedule(token: String, id: String) =
        apiInterface.deleteSchedule("Bearer $token", id)

    /**
     * Operaciones de Tareas de Horario
     */

    /**
     * Crea una nueva tarea en un horario
     * @param token Token de autenticación
     * @param userId ID del usuario
     * @param title Título de la tarea
     * @param content Contenido de la tarea
     * @param priority Prioridad de la tarea
     * @param startTime Hora de inicio
     * @param endTime Hora de fin
     * @param week_day Día de la semana
     * @param scheduleId ID del horario
     * @param categoryId ID de la categoría
     * @return Tarea creada
     */
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

    /**
     * Elimina una tarea de un horario
     * @param token Token de autenticación
     * @param id ID de la tarea
     * @return Respuesta del servidor
     */
    suspend fun deleteTask(token: String, id: String) =
        apiInterface.deleteTask("Bearer $token", id)

    /**
     * Obtiene todas las tareas
     * @param token Token de autenticación
     * @return Lista de tareas
     */
    suspend fun getAllTasks(token: String) =
        apiInterface.getAllTasks("Bearer $token")

    /**
     * Operaciones de Calendario
     */

    /**
     * Obtiene todos los calendarios
     * @param token Token de autenticación
     * @return Lista de calendarios
     */
    suspend fun getAllCalendars(token: String) =
        apiInterface.getAllCalendar("Bearer $token")

    /**
     * Crea un nuevo calendario
     * @param token Token de autenticación
     * @param userId ID del usuario
     * @param title Título del calendario
     * @param isFavorite Si es favorito
     * @param categoryId ID de la categoría
     * @return Calendario creado
     */
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

    /**
     * Obtiene una tarea específica del calendario
     * @param token Token de autenticación
     * @param id ID de la tarea
     * @return Tarea del calendario
     */
    suspend fun getCalendarTask(
        token: String,
        id: String
    ) =
        apiInterface.getCalendarTask("Bearer $token", id)

    /**
     * Obtiene todas las tareas del calendario
     * @param token Token de autenticación
     * @return Lista de tareas del calendario
     */
    suspend fun getAllCalendarTask(token: String) =
        apiInterface.getAllCalendarTask("Bearer $token")

    /**
     * Crea una nueva tarea en el calendario
     * @param token Token de autenticación
     * @param userId ID del usuario
     * @param title Título de la tarea
     * @param content Contenido de la tarea
     * @param isCompleted Estado de completado
     * @param priority Prioridad de la tarea
     * @param startTime Hora de inicio
     * @param endTime Hora de fin
     * @param calendarId ID del calendario
     * @param categoryId ID de la categoría
     * @return Tarea creada
     */
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

    /**
     * Actualiza una tarea del calendario
     * @param token Token de autenticación
     * @param id ID de la tarea
     * @param userId ID del usuario
     * @param title Nuevo título
     * @param content Nuevo contenido
     * @param isCompleted Nuevo estado de completado
     * @param priority Nueva prioridad
     * @param startTime Nueva hora de inicio
     * @param endTime Nueva hora de fin
     * @param calendarId ID del calendario
     * @param categoryId ID de la categoría
     * @return Tarea actualizada
     */
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

    /**
     * Elimina una tarea del calendario
     * @param token Token de autenticación
     * @param id ID de la tarea
     * @return Respuesta del servidor
     */
    suspend fun deleteCalendarTask(token: String, id: String) =
        apiInterface.deleteCalendarTask("Bearer $token", id)
}