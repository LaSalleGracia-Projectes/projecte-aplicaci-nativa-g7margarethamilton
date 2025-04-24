package com.example.projecte_aplicaci_nativa_g7margarethamilton.model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User

abstract class Modul(
    val idModul: Int,
    val nomModul: String,
    val user: User
) {
    abstract fun activar()
    abstract fun desactivar()
}