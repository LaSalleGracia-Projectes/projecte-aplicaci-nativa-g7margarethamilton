package com.example.projecte_aplicaci_nativa_g7margarethamilton.model

data class Usuari(
    var nickname: String,
    var email: String,
    var password: String,
    var google_id: String? = null
)