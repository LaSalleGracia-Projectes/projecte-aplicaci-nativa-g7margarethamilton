package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import Terms
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.GoogleAuthUiClient
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

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
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                .getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.loginWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            // Error al fer login amb Google
        }
    }

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
                color =  MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(top = 120.dp, bottom = 8.dp)
            )

            // Register subtitle
            Text(
                text = "Register",
                fontSize = 18.sp,
                color =  MaterialTheme.colorScheme.onSecondary,
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
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground
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
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground
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
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground
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
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface,
                    focusedBorderColor = MaterialTheme.colorScheme.onBackground
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
                        password = password
                    )
                    viewModel.register(usuari)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.small,
                enabled = correctFormat
            ) {
                Text("Register")
            }

            if (missatgeRegister.isNotEmpty()) {
                Text(
                    text = missatgeRegister,
                    color = if (missatgeRegister == "Usuari registrat") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Or separator
            Text(
                text = "or",
                color =  MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google register button
            Button(
                onClick = {
                    val googleAuthUiClient = GoogleAuthUiClient(context)
                    launcher.launch(googleAuthUiClient.getIntent())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.small,
                enabled = true
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
