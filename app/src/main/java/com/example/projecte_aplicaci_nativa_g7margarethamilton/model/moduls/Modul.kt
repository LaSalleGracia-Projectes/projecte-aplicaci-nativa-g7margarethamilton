package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari

abstract class Modul(
    val idModul: Int,
    val nomModul: String,
    val usuari: Usuari
) {
    abstract fun activar()
    abstract fun desactivar()
}