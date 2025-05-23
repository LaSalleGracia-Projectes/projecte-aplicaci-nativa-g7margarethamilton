package com.example.projecte_aplicaci_nativa_g7margarethamilton.api
import ZonedDateTimeAdapter
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Calendar_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.UserSettings
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.ZonedDateTime

/**
 * Respuesta del servidor para el registro de usuario
 */
data class RegisterResponse(
    val message: String,
    val avatar_url: String
)

/**
 * Respuesta del servidor para el inicio de sesión
 */
data class LoginResponse(
    val tokenApp: String,
    val user: User,
    val message: String
)

/**
 * Interfaz que define todos los endpoints de la API REST.
 * 
 * Esta interfaz proporciona métodos para interactuar con:
 * - Autenticación de usuarios
 * - Gestión de usuarios
 * - Gestión de calendarios
 * - Gestión de horarios
 * - Gestión de tareas
 */
interface ApiService {

    /**
     * Endpoints de Autenticación
     */
    
    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario Datos del usuario a registrar
     * @return Respuesta con mensaje de confirmación y URL del avatar
     */
    @POST("auth/register")
    suspend fun register(
        @Body usuario: User
    ): Response<RegisterResponse>

    /**
     * Inicia sesión de usuario en la aplicación
     * @param usuario Credenciales del usuario
     * @return Respuesta con token, datos del usuario y mensaje
     */
    @POST("auth/app/login")
    suspend fun login(
        @Body usuario: User
    ): Response<LoginResponse>

    /**
     * Inicia sesión usando Google OAuth
     * @param body Mapa con el token de ID de Google
     * @return Respuesta con token, datos del usuario y mensaje
     */
    @POST("auth/app/google")
    suspend fun loginWithGoogle(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: Map<String, String>): Response<Map<String, String>>

    //LOGOUT
    @POST("auth/app/logout")
    suspend fun logoutApp(
        @Body credentials: Map<String, String>
    ): Response<Map<String, String>>

    /**
     * GET dades d'un usuari per email
     */
    @GET("user/{email}")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<User>

    /**
     * PUT actualitzar usuari
     */
    @PUT("user/{email}")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Path("email") email: String,
        @Body request: UpdateUserRequest
    ): Response<UpdateUserResponse>

    /**
     * DELETE usuari
     */
    @DELETE("user/{email}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<MessageResponse>

    @GET("setting/{email}")
    suspend fun getUserSettings(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<UserSettings>

    @PUT("setting/{email}")
    suspend fun updateUserSettings(
        @Header("Authorization") token: String,
        @Path("email") email: String,
        @Body request: UpdateSettingsRequest
    ): Response<UpdateSettingsResponse>

    @POST("auth/contact")
    suspend fun contactUs(
        @Body body: ContactRequest
    ): Response<MessageResponse>

    /**
    SCHEDULE
     */
    //GET ALL SCHEDULES
    @GET("schedule/")
    suspend fun getAllSchedules(
        @Header("Authorization") token: String,
//        @Body userId: String
    ): Response<List<Schedule>>

    //GET SCHEDULE BY ID
    @GET("schedule/{id}")
    suspend fun getSchedule(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<Schedule>

    //CREATE SCHEDULE
    @POST("schedule/")
    suspend fun createSchedule(
        @Header("Authorization") token: String,
        @Body request: CreateScheduleRequest
    ): Response<Schedule>

    //UPDATE SCHEDULE
    @PUT("schedule/{id}")
    suspend fun updateSchedule(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: UpdateScheduleRequest
    ): Response<Schedule>

    //DELETE SCHEDULE
    @DELETE("schedule/{id}")
    suspend fun deleteSchedule(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    //CREATE TASK
    @POST("schedule-task/")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body request: CreateTaskRequest
    ): Response<Schedule_task>

    //GET ALL TASKS
    @GET("schedule-task")
    suspend fun getAllTasks(
        @Header("Authorization") token: String,
    ): Response<List<Schedule_task>>

    //DELETE TASK
    @DELETE("schedule-task/{id}")
    suspend fun deleteTask(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ResponseBody>

    /**
     * CALENDAR
     */
    //GET ALL CALENDAR
    @GET("calendar/")
    suspend fun getAllCalendar(
        @Header("Authorization") token: String,
    ): Response<List<Calendar>>

    //GET CALENDAR BY ID
    @GET("calendar/{id}")
    suspend fun getCalendar(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<Calendar>

    //CREATE CALENDAR
    @POST("calendar/")
    suspend fun createCalendar(
        @Header("Authorization") token: String,
        @Body request: CreateCalendarRequest
    ): Response<Calendar>

    //UPDATE CALENDAR
    @PUT("calendar/{id}")
    suspend fun updateCalendar(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: CreateCalendarRequest
    ): Response<Calendar>

    //DELETE CALENDAR
    @DELETE("calendar/{id}")
    suspend fun deleteCalendar(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    //GET CALENDAR-TASK
    @GET("calendar-task/")
    suspend fun getAllCalendarTask(
        @Header("Authorization") token: String,
    ): Response<List<Calendar_task>>

    //GET CALENDAR-TASK BY ID
    @GET("calendar-task/{id}")
    suspend fun getCalendarTask(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Response<Calendar_task>

    //CREATE CALENDAR-TASK
    @POST("calendar-task/")
    suspend fun createCalendarTask(
        @Header("Authorization") token: String,
        @Body request: CreateCalendarTaskRequest
    ): Response<Calendar_task>

    //UPDATE CALENDAR-TASK
    @PUT("calendar-task/{id}")
    suspend fun updateCalendarTask(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: CreateCalendarTaskRequest
    ): Response<Calendar_task>

    //DELETE CALENDAR-TASK
    @DELETE("calendar-task/{id}")
    suspend fun deleteCalendarTask(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>

    companion object{
        private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"
        //private const val BASE_URL = "http://192.168.195.129:3000/api/v1/"

        fun create(): ApiService {
            val gson = GsonBuilder()
                .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeAdapter())
                .create()

            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

data class ContactRequest(
    val email: String,
    val message: String
)

data class CreateCalendarTaskRequest (
    val userId: String,
    val title: String,
    val content: String,
    val is_completed: Boolean,
    val priority: Int,
    val start_time: String,
    val end_time: String,
    val id_calendar: Int,
    val id_category: Int,
)

data class CreateCalendarRequest (
    val userId: String,
    val title: String,
    val is_favorite: Boolean,
    val id_category: Int
)

data class CreateScheduleRequest(
    val userId: String,
    val title: String,
    val is_favorite: Boolean,
    val id_category: Int
)

data class UpdateScheduleRequest(
    val userId: String,
    val title: String,
    val is_favorite: Boolean,
    val id_category: Int
)

data class CreateTaskRequest(
    val userId: String,
    val title: String,
    val content: String,
    val priority: Int,
    val start_time: String,
    val end_time: String,
    val id_schedule: Int,
    val id_category: Int,
    val week_day: Int,

)

data class UpdateUserRequest(
    val nickname: String?,
    val avatar_url: String?,
    val password: Any?,
    val is_admin: Boolean,
    val is_banned: Boolean
)


data class UpdateUserResponse(
    val message: String,
    val user: User
)

data class MessageResponse(
    val message: String
)

data class UpdateSettingsRequest(
    val theme_mode: Boolean,
    val lang_code: String,
    val allow_notification: Boolean,
    val merge_schedule_calendar: Boolean
)

// Response de l'API de settings
data class UpdateSettingsResponse(
    val message: String,
    val settings: UserSettings
)