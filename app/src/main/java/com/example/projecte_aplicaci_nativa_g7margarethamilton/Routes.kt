package com.example.projecte_aplicaci_nativa_g7margarethamilton

sealed class Routes(val route: String) {
    object Welcome : Routes("welcome")
    object Register : Routes("register")
    object Login : Routes("login")


    // Rutas de la bottom nav
    object Home : Routes("home")
    object Settings : Routes("settings")
    object Profile : Routes("profile")

    object DailyTasks : Routes("daily_tasks")
    object Calendar : Routes("calendar")
    object AboutUs : Routes("about_us")
}