package com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari

abstract class Modul(
    val idModul: Int,
    val nomModul: String,
    val usuari: Usuari
) {
    abstract fun activar()
    abstract fun desactivar()
}