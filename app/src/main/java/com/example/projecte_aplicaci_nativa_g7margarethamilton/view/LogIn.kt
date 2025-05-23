package com.example.projecte_aplicaci_nativa_g7margarethamilton.view

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projecte_aplicaci_nativa_g7margarethamilton.R
import com.example.projecte_aplicaci_nativa_g7margarethamilton.Routes
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.GoogleAuthUiClient
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.UserViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel.setLocale
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

@Composable
fun LogIn(navController: NavController, viewModel: UserViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val correctFormat by viewModel.correctFormat.collectAsState()
    val context = LocalContext.current
    val lang = viewModel.getSavedLanguage(context)
    val localizedContext = context.setLocale(lang)
    var showForgotDialog by rememberSaveable { mutableStateOf(false) }
    var forgotEmail by rememberSaveable { mutableStateOf("") }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        try {
            val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).getResult(ApiException::class.java)
            val idToken = account?.idToken
            if (idToken != null) {
                viewModel.loginWithGoogle(context, idToken)
            }
        } catch (e: ApiException) {
            // Error
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

        if (showForgotDialog) {
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showForgotDialog = false },
                confirmButton = {
                    Button(onClick = {
                        viewModel.sendResetPasswordEmail(forgotEmail, context)
                        showForgotDialog = false
                    }) {
                        Text(localizedContext.getString(R.string.forgot_password_view_send_button))
                    }
                },
                dismissButton = {
                    Button(onClick = { showForgotDialog = false }) {
                        Text(localizedContext.getString(R.string.forgot_password_view_cancel_button))
                    }
                },
                title = { Text(localizedContext.getString(R.string.forgot_password_view_title)) },
                text = {
                    Column {
                        Text(localizedContext.getString(R.string.forgot_password_view_description))
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it },
                            placeholder = { Text(localizedContext.getString(R.string.forgot_password_view_email_placeholder)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
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
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Login subtitle
            Text(
                text = localizedContext.getString(R.string.login_view_title),
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSecondary,
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
                placeholder = { Text(localizedContext.getString(R.string.login_view_email_placeholder)) },
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
                    viewModel.validateLogin(email, password)
                },
                placeholder = { Text(localizedContext.getString(R.string.login_view_password_placeholder)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
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

            // Forgot password text
            Text(
                text = localizedContext.getString(R.string.login_view_forgot_password),
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 24.dp)
                    .clickable { showForgotDialog = true }
            )

            // Login button
            Button(
                onClick = {
                    viewModel.login(context, email, password)
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
                Text(localizedContext.getString(R.string.login_view_login_button))
            }

            val missatgeLogin by viewModel.missatgeLogin.collectAsState()
            if (missatgeLogin.isNotEmpty()) {
                Text(
                    text = missatgeLogin,
                    color = if (missatgeLogin.contains("exitoso")) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            val currentUser by viewModel.currentUser.collectAsState()
            if (currentUser != null) {
                LaunchedEffect(currentUser) {
                    navController.navigate(Routes.TodayTasks.route) {
                        popUpTo(Routes.Login.route) { inclusive = true }
                    }
                }
            }

            // Or separator
            Text(
                text = localizedContext.getString(R.string.login_view_or),
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Google login button
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
                Text(localizedContext.getString(R.string.login_view_google_button))
            }
        }
    }
}
