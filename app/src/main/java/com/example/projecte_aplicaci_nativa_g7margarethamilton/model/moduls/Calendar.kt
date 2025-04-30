package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time
import java.time.ZonedDateTime

class Calendar(
    id: Int,
    title: String,
    is_favorite: Boolean,
    email: String,
    id_category: Int,
    tasks: List<Calendar_task>?,
    created_at: ZonedDateTime
)

class Calendar_task(
    id: Int,
    title: String,
    content: String,
    is_completed: Boolean,
    priority: Int,
    start_time: Time,
    end_time: Time,
    id_calendar: Int,
    id_category: Int,
    created_at: ZonedDateTime,
)

