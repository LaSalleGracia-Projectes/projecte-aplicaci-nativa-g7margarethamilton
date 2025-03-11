package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel.UserViewModel

@Composable
fun LogIn(navController: NavController, viewModel: UserViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val correctFormat by viewModel.correctFormat.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Close button
        IconButton(
            onClick = {
                viewModel.rebootCorrectFormat()
                navController.navigate(Routes.Welcome.route)
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title
            Text(
                text = "Flow2Day!",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E3B4E),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Login subtitle
            Text(
                text = "Login",
                fontSize = 18.sp,
                color = Color(0xFF2E3B4E),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.validateEmail(it)
                    viewModel.validateLogin(email, password)
                },
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                ),
                isError = emailError != null,
                supportingText = { emailError?.let { Text(it) } },
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.validatePassword(it)
                    viewModel.validateLogin(email, password)
                },
                placeholder = { Text("Contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                ),
                isError = passwordError != null,
                supportingText = { passwordError?.let { Text(it) } },
            )

            // Forgot password text
            Text(
                text = "Lorem ipsum sit amet?",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.dp)
            )

            // Login button
            Button(
                onClick = {
                    viewModel.login(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3B4E)
                ),
                shape = MaterialTheme.shapes.small,
                enabled = correctFormat
            ) {
                Text("Login")
            }

            val missatgeLogin by viewModel.missatgeLogin.collectAsState()
            if (missatgeLogin.isNotEmpty()) {
                Text(
                    text = missatgeLogin,
                    color = if (missatgeLogin.contains("exitoso")) Color.Green else Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val currentUser by viewModel.currentUser.collectAsState()
            if (currentUser != null) {
                LaunchedEffect(currentUser) {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            }

            // Or separator
            Text(
                text = "or",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google login button
            Button(
                onClick = {
                    // TODO: Login app amb Google
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7D8A99)
                ),
                shape = MaterialTheme.shapes.small,
                enabled = correctFormat
            ) {
                Text("Login With Google")
            }
        }
    }
}
