package com.example.projecte_aplicaci_nativa_g7margarethamilton.api
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

data class RegisterResponse(
    val message: String,
    val avatar_url: String
)

data class LoginResponse(
    val tokenApp: String,
    val user: UserResponse,
    val message: String
)

data class UserResponse(
    val email: String,
    val nickname: String,
    val avatar_url: String
)

interface ApiService {

    //REGISTER
    @POST("auth/register")
    suspend fun register(
        @Body usuario: Usuari
    ): Response<RegisterResponse>

    //LOGIN
    @POST("auth/app/login")
    suspend fun login(
        @Body usuario: Usuari
    ): Response<LoginResponse>

    //GET ALL SCHEDULES
    @GET("schedule/")
    suspend fun getAllSchedules(
        @Body userId: String
    ): Response<List<Schedule>>

    //GET SCHEDULE BY ID
    @GET("schedule/{id}")
    suspend fun getSchedule(
        @Path("id") id: String,
        @Body idUsuari: String
    ): Response<Schedule>

    //CREATE SCHEDULE
    @POST("schedule/")
    suspend fun createSchedule(
        @Body schedule: Schedule
    ): Response<Any>

    //UPDATE SCHEDULE
    @POST("schedule/{id}")
    suspend fun updateSchedule(
        @Path("id") id: String,
        @Body schedule: Schedule
    ): Response<Any>

    //DELETE SCHEDULE
    @DELETE("schedule/{id}")
    suspend fun deleteSchedule(
        @Path("id") id: String,
        @Body idUsuari: String

    ): Response<Any>



    companion object{
        private const val BASE_URL = "http://10.0.2.2:3000/api/v1/"
                                        //localhost se refiere a la maquina simulada, esta direccion es la del host

        fun create(): ApiService {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}