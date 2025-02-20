package com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.moduls

import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari

class LlistaCompra(
    idModul: Int,
    nomModul: String,
    usuari: Usuari,
    var llistes: List<Llista> = listOf()
) : Modul(idModul, nomModul, usuari) {

    override fun activar() {
        //TODO: Implementación de activar
    }

    override fun desactivar() {
        //TODO: Implementación de desactivar
    }

    fun afegirProducte(idLlista: Int, producte: Producte) {
        //TODO: Implementación de afegirProducte
    }

    fun eliminarProducte(idLlista: Int, idProducte: Int) {
        //TODO: Implementación de eliminarProducte
    }
}

class Producte(
    var idProducte: Int,
    var nom: String,
    var tipus: String,
    var buyed: Boolean
)

class Llista(
    var produtes: List<Producte>
)
