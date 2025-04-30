package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time
import java.time.ZonedDateTime

data class Calendar(
    val id: Int,
    val title: String,
    val is_favorite: Boolean,
    val email: String,
    val id_category: Int,
    val tasks: List<Calendar_task>?,
    val created_at: ZonedDateTime
)

data class Calendar_task(
    val id: Int,
    val title: String,
    val content: String,
    val is_completed: Boolean,
    val priority: Int,
    val start_time: Time,
    val end_time: Time,
    val id_calendar: Int,
    val id_category: Int,
    val created_at: ZonedDateTime,
)

