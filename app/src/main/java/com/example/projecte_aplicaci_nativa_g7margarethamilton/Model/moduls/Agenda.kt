package com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari
import java.sql.Time
import java.util.Date

class Agenda(
    idModul: Int,
    nomModul: String,
    usuari: Usuari,
    var tasques: List<Tasca> = listOf()
) : Modul(idModul, nomModul, usuari) {

    override fun activar() {
        //TODO: Implementación de activar
    }

    override fun desactivar() {
        //TODO: Implementación de desactivar
    }

    fun afegirTasca(tasca: Tasca) {
        //TODO: Implementación de afegirTasca
    }

    fun editarTasca(id: Int) {
        //TODO: Implementación de editarTasca
    }

    fun eliminarTasca(id: Int) {
        //TODO: Implementación de eliminarTasca
    }
}

class Tasca(
    idTasca: Int,
    date: Date,
    horaInici: Time,
    horaFinal: Time
)
