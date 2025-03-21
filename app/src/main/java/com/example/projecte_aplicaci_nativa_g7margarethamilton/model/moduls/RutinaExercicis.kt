package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari

class RutinaExercicis(
    idModul: Int,
    nomModul: String,
    usuari: Usuari,
    var rutines: List<Rutina> = listOf()
) : Modul(idModul, nomModul, usuari) {

    override fun activar() {
        //TODO: Implementación de activar
    }

    override fun desactivar() {
        //TODO: Implementación de desactivar
    }

    fun editarRutina(id: Int) {
        //TODO: Implementación de editarRutina
    }

    fun eliminarRutina(id: Int) {
        //TODO: Implementación de eliminarRutina
    }
}

class Rutina {

}
