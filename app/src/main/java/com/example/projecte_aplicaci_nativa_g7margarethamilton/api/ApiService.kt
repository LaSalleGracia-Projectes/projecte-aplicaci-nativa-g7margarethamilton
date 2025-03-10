package com.example.projecte_aplicaci_nativa_g7margarethamilton.api
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body

data class RegisterResponse(
    val message: String,
    val avatar_url: String
)

interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body usuario: Usuari
    ): Response<RegisterResponse>



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