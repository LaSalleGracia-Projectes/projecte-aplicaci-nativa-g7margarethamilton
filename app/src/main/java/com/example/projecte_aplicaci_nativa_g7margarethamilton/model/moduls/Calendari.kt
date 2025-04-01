package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari

class Calendari(
    idModul: Int,
    nomModul: String,
    usuari: Usuari,
    var esdeveniments: List<Esdeveniment> = listOf()
) : Modul(idModul, nomModul, usuari) {

    override fun activar() {
        //TODO: Implementación de activar
    }

    override fun desactivar() {
        //TODO: Implementación de desactivar
    }

    fun afegirEsdeveniment(esdeveniment: Esdeveniment) {
        //TODO: Implementación de afegirEsdeveniment
    }

    fun editarEsdeveniment(id: Int) {
        //TODO: Implementación de editarEsdeveniment
    }

    fun eliminarEsdeveniment(id: Int) {
        //TODO: Implementación de eliminarEsdeveniment
    }
}

class Esdeveniment {

}
