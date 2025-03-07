package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import Terms
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
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Model.Usuari

@Composable
fun SignIn(navController: NavController, viewModel: UserViewModel) {
    var nickname by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val nicknameError by viewModel.nicknameError.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val correctFormat by viewModel.correctFormat.collectAsState()
    val missatgeRegister by viewModel.missatgeRegister.collectAsState()

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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App title
            Text(
                text = "Flow2Day!",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E3B4E),
                modifier = Modifier.padding(top = 120.dp, bottom = 8.dp)
            )

            // Register subtitle
            Text(
                text = "Register",
                fontSize = 18.sp,
                color = Color(0xFF2E3B4E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Name field
            OutlinedTextField(
                value = nickname,
                onValueChange = {
                    nickname = it
                    viewModel.validateNickname(it)
                    viewModel.validateSignUp(nickname, email, password, confirmPassword)
                },
                placeholder = { Text("Nombre") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                ),
                isError = nicknameError != null,
                supportingText = { nicknameError?.let { Text(it) } },
            )

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.validateEmail(it)
                    viewModel.validateSignUp(nickname, email, password, confirmPassword)
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
                    viewModel.validateSignUp(nickname, email, password, confirmPassword)
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

            // Repeat Password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    viewModel.validateConfirmPassword(password, it)
                    viewModel.validateSignUp(nickname, email, password, confirmPassword)
                },
                placeholder = { Text("Repite la contraseña") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                ),
                isError = confirmPasswordError != null,
                supportingText = { confirmPasswordError?.let { Text(it) } },
                )

            // Register button
            Button(
                onClick = { 
                    val usuari = Usuari(
                        nickname = nickname,
                        email = email,
                        contrasenya = password
                    )
                    viewModel.register(usuari)
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
                Text("Register")
            }

            if (missatgeRegister.isNotEmpty()) {
                Text(
                    text = missatgeRegister,
                    color = if (missatgeRegister == "Usuari registrat") Color.Green else Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Or separator
            Text(
                text = "or",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google register button
            Button(
                onClick = {
                    // TODO: Register amb Google
                    // TODO: Validate register
                    // TODO: Login app amb Google
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7D8A99)
                ),
                shape = MaterialTheme.shapes.small,
                enabled = false
            ) {
                Text("Register With Google")
            }

            Terms()
        }
    }
}

//@SuppressLint("ViewModelConstructorInComposable")
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SignInPreview() {
//    val validator = UserViewModel()
//    SignIn(validator)
//}
