package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time

data class Schedule(
    val id: Int,
    val title: String,
    val is_favorite: Boolean,
    val email: String,
    val id_category: Int,
    val created_at: Time,
    val tasks: List<Schedule_task>
)

data class Schedule_task(
    val id: Int,
    val title: String,
    val content: String,
    val priority: Int,
    val start_time: String,
    val end_time: String,
    val id_schedule: Int,
    val id_category: Int,
    val created_at: Time
)