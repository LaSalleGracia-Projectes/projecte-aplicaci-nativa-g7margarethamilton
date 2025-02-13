package com.example.projecte_aplicaci_nativa_g7margarethamilton.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel.UserViewModel
import Terms
import android.annotation.SuppressLint
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

/*
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
*/



@Composable
fun SignIn(validator: UserViewModel) {
    var name by rememberSaveable { mutableStateOf("") }
    var surName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var checkedTerms by rememberSaveable { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val nameError by validator.nameError.collectAsState()
    val emailError by validator.emailError.collectAsState()
    val passwordError by validator.passwordError.collectAsState()
    val confirmPasswordError by validator.confirmPasswordError.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)  // Espaciado uniforme entre elementos
    ) {
        Text(
            text = "Flow2Day!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 40.dp, bottom = 16.dp)
        )

        Text(
            text = "Crea un compte",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(
            text = "Introdueix les teves dades",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )


        CustomOutlinedTextField(
            modifier = Modifier,
            name,
            onValueChange = {
                name = it  // Actualiza el valor
                validator.validateName(it)  // Valida el nuevo valor
            },
            "Ex: Joan",
            nameError,
            false
            )
        CustomOutlinedTextField(
            modifier = Modifier,
            surName,
            onValueChange = {
                surName = it  // Actualiza el valor
                validator.validateSurName(it)  // Valida el nuevo valor
            },
            "Ex: Doe",
            null,
            false
        )
        CustomOutlinedTextField(
            modifier = Modifier,
            email,
            onValueChange = {
                email = it  // Actualiza el valor
                validator.validateEmail(it)  // Valida el nuevo valor
            },
            "email@domain.com",
            emailError,
            false,
            KeyboardType.Email
        )

        CustomOutlinedTextField(
            modifier = Modifier,
            password,
            onValueChange = {
                password = it  // Actualiza el valor
                validator.validatePassword(it)  // Valida el nuevo valor
            },
            "Contrasenya",
            passwordError,
            true,
            KeyboardType.Password
        )

        CustomOutlinedTextField(
            modifier = Modifier,
            confirmPassword,
            onValueChange = {
                confirmPassword = it  // Actualiza el valor
                validator.validateConfirmPassword(password, it)
            },
            "Confirma la contrasenya",
            confirmPasswordError,
            true,
            KeyboardType.Password
        )



        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Registra't")
        }

        Text(
            text = "or",
            modifier = Modifier.padding(vertical = 16.dp),
            color = Color.Gray
        )

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray
            )
        ) {
            Text("Registra't amb Google", color = Color.Black)
        }

        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black
            )
        ) {
            Text("Ja tinc un compte")
        }

        Terms()
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    val validator = UserViewModel()
    SignIn(validator)
}