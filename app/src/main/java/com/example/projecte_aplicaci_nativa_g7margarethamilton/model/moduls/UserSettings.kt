package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

data class UserSettings(
    val email: String,
    val theme_mode: Boolean,
    val lang_code: String,
    val allow_notification: Boolean,
    val merge_schedule_calendar: Boolean,
    val created_at: String
)