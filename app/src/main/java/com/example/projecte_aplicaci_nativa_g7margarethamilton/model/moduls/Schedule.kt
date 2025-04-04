package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time

class Schedule(
    id: Int,
    title: String,
    is_favorite: Boolean,
    email: String,
    id_category: Int,
    created_at: Time
)

class Schedule_task(
    val id: Int,
    val title: String,
    val content: String,
    val is_completed: Boolean,
    val priority: Int,
    val start_time: String,
    val end_time: String,
    val id_calendar: Int,
    val id_category: Int,
    val created_at: Time
)