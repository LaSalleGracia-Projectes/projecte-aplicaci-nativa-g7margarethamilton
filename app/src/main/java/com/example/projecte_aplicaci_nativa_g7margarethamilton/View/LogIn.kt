package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel.UserViewModel

@Composable
fun LogIn(navController: NavController, validator: UserViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val emailError by validator.emailError.collectAsState()
    val passwordError by validator.passwordError.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Close button
        // Close button
        IconButton(
            onClick = { navController.navigate(Routes.Welcome.route) },
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
                    validator.validateEmail(it)
                },
                placeholder = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                )
            )

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    validator.validatePassword(it)
                },
                placeholder = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                )
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
                onClick = { /* TODO */ },
                enabled = if (email.isNotEmpty() || password.isNotEmpty()) {
                    true
                } else {
                    false
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3B4E)
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Login")
            }

            // Or separator
            Text(
                text = "or",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google login button
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7D8A99)
                ),
                shape = MaterialTheme.shapes.small
            ) {
                Text("Login With Google")
            }
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginPreview() {
//    val validator = UserViewModel()
//    LogIn(validator)
//}