package com.example.projecte_aplicaci_nativa_g7margarethamilton

sealed class Routes(val route: String) {
    object Welcome : Routes("welcome")
    object Register : Routes("register")
    object Login : Routes("login")


    // Rutas de la bottom nav
    object Home : Routes("home")
    object Settings : Routes("settings")
    object Profile : Routes("profile")

    object Schedule : Routes("schedule")
    object Calendar : Routes("calendar")
    object AboutUs : Routes("about_us")
    object ContactUs : Routes("contact_us")
    object ProfileSettings : Routes("profile_settings")
    object EditProfile : Routes("edit_profile")
    object Exercises : Routes("exercises")
    object ShoppingList : Routes("shopping_list")
    object TodayTasks : Routes("today_tasks")
}