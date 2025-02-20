package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel.UserViewModel


@Composable
fun EntryPoint(navController: NavHostController, viewModel: UserViewModel){

    NavHost(
        navController = navController,
        startDestination = Routes.Welcome.route
    ){
        composable(Routes.Welcome.route) { WelcomeView(navController) }
        composable(Routes.Login.route) { LogIn(navController, viewModel) }
        composable(Routes.Register.route) { SignIn(navController, viewModel)  }
    }
}