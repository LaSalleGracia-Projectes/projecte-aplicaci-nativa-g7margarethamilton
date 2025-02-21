package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import Terms
import android.annotation.SuppressLint
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel.UserViewModel

@Composable
fun SignIn(navController: NavController, validator: UserViewModel) {
    var name by rememberSaveable { mutableStateOf("") }
    var surName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    val nameError by validator.nameError.collectAsState()
    val emailError by validator.emailError.collectAsState()
    val passwordError by validator.passwordError.collectAsState()
    val confirmPasswordError by validator.confirmPasswordError.collectAsState()
    val surNameError by validator.surnameError.collectAsState()

    var esValido =
        if (name.isNotEmpty() && surName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() &&
            nameError!!.isEmpty() && emailError!!.isEmpty() && passwordError!!.isEmpty() && confirmPasswordError!!.isEmpty() && surNameError!!.isEmpty()) {
            true
        } else {
            false
        }

    Box(modifier = Modifier.fillMaxSize()) {
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
                value = name,
                onValueChange = {
                    name = it
                    validator.validateName(it)
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
                isError = nameError != null,
                supportingText = { nameError?.let { Text(it) } },
            )

            // First Name field
            OutlinedTextField(
                value = surName,
                onValueChange = {
                    surName = it
                },
                placeholder = { Text("Apellido") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color.Gray
                ),
                isError = surNameError != null,
                supportingText = { surNameError?.let { Text(it) } },
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
                    validator.validatePassword(it)
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
                    validator.validateConfirmPassword(password, it)
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
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E3B4E)
                ),
                shape = MaterialTheme.shapes.small,
                enabled = esValido
            ) {
                Text("Register")
            }

            // Or separator
            Text(
                text = "or",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google register button
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
