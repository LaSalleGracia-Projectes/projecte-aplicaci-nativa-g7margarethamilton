package com.example.projecte_aplicaci_nativa_g7margarethamilton.model

data class User(
    val email: String,
    val google_id: String?,
    val password: String,
    val nickname: String,
    val avatar_url: String,
    val is_admin: Boolean,
    val is_banned: Boolean,
    val web_token: String?,
    val app_token: String,
    val created_at: String
)