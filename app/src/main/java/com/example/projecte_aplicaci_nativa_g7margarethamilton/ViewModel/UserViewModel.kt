package com.example.projecte_aplicaci_nativa_g7margarethamilton.ViewModel


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class UserViewModel : ViewModel() {
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError = _nameError.asStateFlow()

    private val _surnameError = MutableStateFlow<String?>(null)
    val surnameError = _surnameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    fun validateName(name: String): Boolean {
        return if (name.length < 2) {
            _nameError.value = "El nombre debe tener al menos 2 caracteres"
            false
        } else {
            _nameError.value = null
            true
        }
    }

    fun validateEmail(email: String): Boolean {
        return if (!email.matches(Regex("[^@]+@[^@]+\\.[^@]+"))) {
            _emailError.value = "Email no válido"
            false
        } else {
            _emailError.value = null
            true
        }
    }


    fun validatePassword(password: String): Boolean {
        return if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))) {
            _passwordError.value =
                "La contraseña deben ser 9 dígitos y contener al menos una letra y un número"
            false
        } else {
            _passwordError.value = null
            true
        }
    }

    fun validateConfirmPassword(password1: String, password2: String): Boolean {
        return if (password1 != password2) {
            _confirmPasswordError.value = "Las contraseñas deben coincidir"
            false
        } else {
            _confirmPasswordError.value = null
            true
        }
    }


}