package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import java.sql.Time

class Calendar(
    id: Int,
    title: String,
    is_favorite: Boolean,
    id_category: Int,
    created_at: Time
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
    created_at: Time,
)

