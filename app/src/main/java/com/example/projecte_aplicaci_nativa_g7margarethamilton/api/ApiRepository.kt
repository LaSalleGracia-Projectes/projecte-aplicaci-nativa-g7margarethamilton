package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari

class ApiRepository {
    val apiInterface = ApiService.create()

    suspend fun register(usuari: Usuari) = apiInterface.register(usuari)
}