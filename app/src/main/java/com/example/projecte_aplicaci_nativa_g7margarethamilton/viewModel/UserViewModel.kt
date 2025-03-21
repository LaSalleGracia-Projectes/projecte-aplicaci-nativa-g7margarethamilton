package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import androidx.lifecycle.ViewModel
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.Usuari
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel : ViewModel() {
    private val _nicknameError = MutableStateFlow<String?>(null)
    val nicknameError = _nicknameError.asStateFlow()
    private var nicknameValid = false

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()
    private var emailValid = false

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()
    private var passwordValid = false

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()
    private var confirmPasswordValid = false

    var _correctFormat = MutableStateFlow(false)
    val correctFormat = _correctFormat.asStateFlow()

    private val _missatgeRegister = MutableStateFlow("")
    val missatgeRegister: StateFlow<String> = _missatgeRegister

    private val _missatgeLogin = MutableStateFlow("")
    val missatgeLogin: StateFlow<String> = _missatgeLogin

    private val _currentUser = MutableStateFlow<UserResponse?>(null)
    val currentUser: StateFlow<UserResponse?> = _currentUser

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    val repository = ApiRepository()

    fun validateNickname(nickname: String): Boolean {
        if (nickname.length < 2) {
            _nicknameError.value = "El nombre debe tener al menos 2 caracteres"
            nicknameValid = false
            return false
        } else {
            _nicknameError.value = null
            nicknameValid = true
            return true
        }
    }

    fun validateEmail(email: String): Boolean {
        if (!email.matches(Regex("[^@]+@[^@]+\\.[^@]+")) && email.isNotEmpty()) {
            _emailError.value = "Email no válido"
            emailValid = false
            return false
        } else {
            _emailError.value = null
            emailValid = true
            return true
        }
    }

    fun validatePassword(password: String): Boolean {
        if (!password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{9,}$"))) {
            _passwordError.value = "La contraseña debe tener al menos 8 caracteres y contener al menos una letra y un número"
            passwordValid = false
            return false
        } else {
            _passwordError.value = null
            passwordValid = true
            return true
        }
    }

    fun validateConfirmPassword(password1: String, password2: String): Boolean {
        if (password1 != password2) {
            _confirmPasswordError.value = "Las contraseñas deben coincidir"
            confirmPasswordValid = false
            return false
        } else {
            _confirmPasswordError.value = null
            confirmPasswordValid = true
            return true
        }
    }

    fun validateLogin(email: String, password: String) {
        if ((!email.matches(Regex("[^@]+@[^@]+\\.[^@]+")) && email.isNotEmpty()) || !password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))) {
            _correctFormat.value = false
        } else {
            _correctFormat.value = true
        }
    }

    fun validateSignUp(nickname: String, email: String, password: String, confirmPassword: String) {
        if (nickname.length < 2 || (!email.matches(Regex("[^@]+@[^@]+\\.[^@]+")) && email.isNotEmpty()) || !password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) || password != confirmPassword) {
            _correctFormat.value = false
        } else {
            _correctFormat.value = true
        }
    }

    fun rebootCorrectFormat() {
        _correctFormat.value = false
        _nicknameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    fun register(usuari: Usuari){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.register(usuari)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        val registerResponse = response.body()
                        _missatgeRegister.value = registerResponse?.message ?: "Usuario registrado exitosamente"
                    } else {
                        when (response.code()) {
                            409 -> _missatgeRegister.value = "El usuario ya existe"
                            400 -> _missatgeRegister.value = "Datos de registro inválidos"
                            else -> _missatgeRegister.value = "Error en el registro"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _missatgeRegister.value = "Error de conexión: ${e.message}"
                }
            }
        }
    }
    fun clearMissatgeRegister() {
        _missatgeRegister.value = ""
    }

    fun login(email: String, password: String) {
        val usuari = Usuari(
            nickname = "",  // No necesario para login
            email = email,
            password = password
        )
        
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.login(usuari)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _token.value = loginResponse?.tokenApp
                        _currentUser.value = loginResponse?.user
                        _missatgeLogin.value = loginResponse?.message ?: "Inicio de sesión exitoso"
                    } else {
                        when (response.code()) {
                            401 -> _missatgeLogin.value = "Credenciales incorrectas"
                            404 -> _missatgeLogin.value = "Usuario no encontrado"
                            else -> _missatgeLogin.value = "Error en el inicio de sesión"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _missatgeLogin.value = "Error de conexión: ${e.message}"
                }
            }
        }
    }

    fun clearMissatgeLogin() {
        _missatgeLogin.value = ""
    }

    fun logout() {
        _currentUser.value = null
        _token.value = null
    }
}
