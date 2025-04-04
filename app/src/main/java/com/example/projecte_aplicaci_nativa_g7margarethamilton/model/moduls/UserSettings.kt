package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time

class UserSettings(
    email: String,
    theme_mode: Boolean,
    lang_code: String,
    allow_notifications: Boolean,
    merge_schedule_calendar: Boolean,
    created_at: Time
)