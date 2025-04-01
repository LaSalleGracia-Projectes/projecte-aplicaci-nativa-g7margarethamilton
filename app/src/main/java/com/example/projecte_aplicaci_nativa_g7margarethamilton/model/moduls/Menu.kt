package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari

class Menu(
    idModul: Int,
    nomModul: String,
    usuari: Usuari,
    var menus: List<Menu> = listOf()
) : Modul(idModul, nomModul, usuari) {

    override fun activar() {
        //TODO: Implementación de activar
    }

    override fun desactivar() {
        //TODO: Implementación de desactivar
    }

    fun afegirMenu(menu: Menu) {
        //TODO: Implementación de afegirMenu
    }

    fun editarMenu(id: Int) {
        //TODO: Implementación de editarMenu
    }

    fun eliminarMenu(id: Int) {
        //TODO: Implementación de eliminarMenu
    }
}