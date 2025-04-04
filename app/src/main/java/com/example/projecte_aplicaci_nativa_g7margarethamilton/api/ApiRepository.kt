package com.example.projecte_aplicaci_nativa_g7margarethamilton.api

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls.Schedule

class ApiRepository {
    val apiInterface = ApiService.create()

    //AUTH
    suspend fun register(usuari: Usuari) = apiInterface.register(usuari)
    suspend fun login(usuari: Usuari) = apiInterface.login(usuari)

    //SCHEDULE
    suspend fun getAllSchedules(userId: String) = apiInterface.getAllSchedules(userId)
    suspend fun getSchedule(id: String, idUsuari: String) = apiInterface.getSchedule(id, idUsuari)
    suspend fun createSchedule(schedule: Schedule) = apiInterface.createSchedule(schedule)
    suspend fun updateSchedule(id: String, schedule: Schedule) = apiInterface.updateSchedule(id, schedule)
    suspend fun deleteSchedule(id: String, idUsuari: String) = apiInterface.deleteSchedule(id, idUsuari)
}