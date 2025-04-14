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
import com.example.projecte_aplicaci_nativa_g7margarethamilton.view.settings.EditProfileView
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EntryPoint(navController: NavHostController, viewModel: UserViewModel){

    NavHost(
        navController = navController,
        startDestination = Routes.Welcome.route
    ){
        composable(Routes.Welcome.route) { WelcomeView(navController) }
        composable(Routes.Login.route) { LogIn(navController, viewModel) }
        composable(Routes.Register.route) { SignIn(navController, viewModel)  }
        

        composable(Routes.Home.route) { HomeView(navController) }
        composable(Routes.Settings.route) { SettingsView(navController) }
        composable(Routes.Profile.route) { ProfileView(navController, viewModel) }
        composable(Routes.Schedule.route) { ScheduleView(navController, viewModel) }
        composable(Routes.AboutUs.route){ AboutUsView(navController)  }
        composable(Routes.ContactUs.route){ ContactUsView(navController) }
        composable(Routes.Calendar.route) { CalendarView(navController) }
        composable(Routes.ProfileSettings.route) { ProfileSettingsView(navController) }
        composable(Routes.EditProfile.route) { EditProfileView(navController, viewModel) }
    }
}


