package com.example.projecte_aplicaci_nativa_g7margarethamilton.api
import ZonedDateTimeAdapter
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule_task
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.UserSettings
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.PUT
import java.time.ZonedDateTime

data class RegisterResponse(
    val message: String,
    val avatar_url: String
)

data class LoginResponse(
    val tokenApp: String,
    val user: User,
    val message: String
)

interface ApiService {

    //REGISTER
    @POST("auth/register")
    suspend fun register(
        @Body usuario: User
    ): Response<RegisterResponse>

    //LOGIN
    @POST("auth/app/login")
    suspend fun login(
        @Body usuario: User
    ): Response<LoginResponse>

    //LOGIN WITH GOOGLE
    @POST("auth/app/google")
    suspend fun loginWithGoogle(
        @Body body: Map<String, String>
    ): Response<LoginResponse>

    //LOGOUT
    @POST("auth/app/logout")
    suspend fun logoutApp(
        @Body credentials: Map<String, String>
    ): Response<Map<String, String>>

    /**
     * GET dades d’un usuari per email
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
        @Body request: ApiRepository.UpdateUserRequest
    ): Response<UpdateUserResponse>

    /**
     * DELETE usuari
     */
    @DELETE("user/{email}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<DeleteUserResponse>

    // GET /api/v1/setting/:email
    @GET("setting/{email}")
    suspend fun getSettings(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<UserSettings>

    // PUT /api/v1/setting/:email
    @PUT("setting/{email}")
    suspend fun updateSettings(
        @Header("Authorization") token: String,
        @Path("email") email: String,
        @Body request: ApiRepository.UpdateSettingsRequest
    ): Response<UserSettings>

    // DELETE /api/v1/setting/:email
    @DELETE("setting/{email}")
    suspend fun deleteSettings(
        @Header("Authorization") token: String,
        @Path("email") email: String
    ): Response<Unit>

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

    @GET("schedule-task")
    suspend fun getAllTasks(
        @Header("Authorization") token: String,
    ): Response<List<Schedule_task>>

    companion object{
        private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"
        //private const val BASE_URL = "http://192.168.195.129:3000/api/v1/"

        @RequiresApi(Build.VERSION_CODES.O)
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
    val id_shedule: Int,
    val id_category: Int
)

data class UpdateUserRequest(
    val nickname: String?,
    val avatar_url: String?,
    val is_admin: Boolean,
    val is_banned: Boolean
)

data class UpdateUserResponse(
    val message: String,
    val user: User
)

data class DeleteUserResponse(
    val message: String
)