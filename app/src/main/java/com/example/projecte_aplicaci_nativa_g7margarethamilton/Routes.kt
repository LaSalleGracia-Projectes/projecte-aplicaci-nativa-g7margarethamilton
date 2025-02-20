package com.example.projecte_aplicaci_nativa_g7margarethamilton

sealed class Routes(val route: String) {
object Welcome : Routes("welcome")
object Register : Routes("register")
object Login : Routes("login")

}