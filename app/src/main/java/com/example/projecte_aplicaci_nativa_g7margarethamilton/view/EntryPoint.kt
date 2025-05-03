package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings.AboutUsView
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings.ContactUsView
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings.ProfileSettingsView
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.CalendarViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EntryPoint(
    navController: NavHostController, userViewModel: UserViewModel,
    calendarViewModel: CalendarViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Welcome.route
    ) {
        composable(Routes.Welcome.route) { WelcomeView(navController) }
        composable(Routes.Login.route) { LogIn(navController, userViewModel) }
        composable(Routes.Register.route) { SignIn(navController, userViewModel) }


        composable(Routes.Home.route) { HomeView(navController, viewModel) }
        composable(Routes.Settings.route) { SettingsView(viewModel, navController) }
        composable(Routes.Profile.route) { ProfileView(navController, viewModel) }
        composable(Routes.Schedule.route) { ScheduleView(navController, viewModel) }
        composable(Routes.AboutUs.route) { AboutUsView(viewModel, navController) }
        composable(Routes.ContactUs.route) { ContactUsView(navController) }
        composable(Routes.Calendar.route) { CalendarView(navController,calendarViewModel, userViewModel) }
        composable(Routes.ProfileSettings.route) { ProfileSettingsView(navController, viewModel) }
        composable(Routes.EditProfile.route) { EditProfileView(navController, viewModel) }
        composable(Routes.Exercises.route) { ExercisesView(viewModel, navController) }
        composable(Routes.ShoppingList.route) { ShoppingListView(viewModel, navController) }
    }
}
