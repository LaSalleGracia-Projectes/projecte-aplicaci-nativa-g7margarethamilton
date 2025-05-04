package com.example.projecte_aplicaci_nativa_g7margarethamilton.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.User
import com.example.projecte_aplicaci_nativa_g7margarethamilton.api.ApiRepository
import com.example.projecte_aplicaci_nativa_g7margarethamilton.model.GoogleAuthUiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel principal para la gestión de usuarios y autenticación.
 * 
 * Este ViewModel maneja todas las operaciones relacionadas con usuarios, incluyendo:
 * - Autenticación (registro, inicio de sesión, cierre de sesión)
 * - Gestión de perfil de usuario
 * - Gestión de configuraciones de usuario
 * - Validación de datos de usuario
 */
class UserViewModel : ViewModel() {
    /**
     * Estados para validación de campos
     */
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

    /**
     * Estado para nueva contraseña en actualización de perfil
     */
    val newPassword = MutableStateFlow("")

    /**
     * Estados de validación y mensajes del sistema
     */
    var _correctFormat = MutableStateFlow(false)
    val correctFormat = _correctFormat.asStateFlow()

    private val _missatgeRegister = MutableStateFlow("")
    val missatgeRegister: StateFlow<String> = _missatgeRegister

    private val _missatgeLogin = MutableStateFlow("")
    val missatgeLogin: StateFlow<String> = _missatgeLogin

    /**
     * Estados del usuario actual y token de autenticación
     */
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> = _token

    val repository = ApiRepository()

    /**
     * Estados para actualización de perfil
     */
    val nicknameField  = MutableStateFlow("")
    val avatarUrlField = MutableStateFlow("")

    private val _updateMsg   = MutableStateFlow<String?>(null)
    val updateMsg: StateFlow<String?> = _updateMsg.asStateFlow()

    private val _updateError = MutableStateFlow<String?>(null)
    val updateError: StateFlow<String?> = _updateError.asStateFlow()

    /**
     * Estados para configuraciones de usuario
     */
    val themeModeField             = MutableStateFlow(false)
    val langCodeField              = MutableStateFlow("en")
    val allowNotificationField     = MutableStateFlow(false)
    val mergeScheduleCalendarField = MutableStateFlow(false)

    private val _settingsMsg   = MutableStateFlow<String?>(null)
    val settingsMsg: StateFlow<String?>   = _settingsMsg.asStateFlow()

    private val _settingsError = MutableStateFlow<String?>(null)
    val settingsError: StateFlow<String?> = _settingsError.asStateFlow()

    val isSettingsLoaded = MutableStateFlow(false)

    /**
     * Estados para gestión de idioma
     */
    var hasForcedEnglish by mutableStateOf(false)
    var userHasSelectedLang by mutableStateOf(false)

    /**
     * Valida el nombre de usuario
     * @param nickname Nombre de usuario a validar
     * @return true si el nombre es válido, false en caso contrario
     */
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

    /**
     * Valida el formato del email
     * @param email Email a validar
     * @return true si el email es válido, false en caso contrario
     */
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

    /**
     * Valida la contraseña según los criterios de seguridad
     * @param password Contraseña a validar
     * @return true si la contraseña cumple los criterios, false en caso contrario
     */
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

    /**
     * Valida que las contraseñas coincidan
     * @param password1 Primera contraseña
     * @param password2 Segunda contraseña
     * @return true si las contraseñas coinciden, false en caso contrario
     */
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

    /**
     * Valida las credenciales de inicio de sesión
     * @param email Email del usuario
     * @param password Contraseña del usuario
     */
    fun validateLogin(email: String, password: String) {
        if ((!email.matches(Regex("[^@]+@[^@]+\\.[^@]+")) && email.isNotEmpty()) || !password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$"))) {
            _correctFormat.value = false
        } else {
            _correctFormat.value = true
        }
    }

    /**
     * Valida los datos de registro
     * @param nickname Nombre de usuario
     * @param email Email
     * @param password Contraseña
     * @param confirmPassword Confirmación de contraseña
     */
    fun validateSignUp(nickname: String, email: String, password: String, confirmPassword: String) {
        if (nickname.length < 2 || (!email.matches(Regex("[^@]+@[^@]+\\.[^@]+")) && email.isNotEmpty()) || !password.matches(Regex("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) || password != confirmPassword) {
            _correctFormat.value = false
        } else {
            _correctFormat.value = true
        }
    }

    /**
     * Reinicia el estado de validación del formulario
     */
    fun rebootCorrectFormat() {
        _correctFormat.value = false
        _nicknameError.value = null
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }

    /**
     * Registra un nuevo usuario
     * @param user Datos del usuario a registrar
     */
    fun register(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.register(user)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
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

    /**
     * Limpia el mensaje de registro
     */
    fun clearMissatgeRegister() {
        _missatgeRegister.value = ""
    }

    /**
     * Inicia sesión con credenciales normales
     * @param context Contexto de la aplicación
     * @param email Email del usuario
     * @param password Contraseña del usuario
     */
    fun login(context: Context, email: String, password: String) {
        val user = User(
            nickname = "",
            email = email,
            password = password,
            google_id = null,
            avatar_url = "",
            is_admin = false,
            is_banned = false,
            web_token = "",
            app_token = "",
            created_at = ""
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.login(user)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _token.value = loginResponse?.tokenApp
                        _currentUser.value = loginResponse?.user
                        _missatgeLogin.value = loginResponse?.message ?: "Inicio de sesión exitoso"
                        loadSettings(context)
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

    /**
     * Limpia el mensaje de inicio de sesión
     */
    fun clearMissatgeLogin() {
        _missatgeLogin.value = ""
    }

    /**
     * Inicia sesión con Google
     * @param context Contexto de la aplicación
     * @param idToken Token de identificación de Google
     */
    fun loginWithGoogle(context: Context, idToken: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = repository.loginWithGoogle(idToken)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        _token.value = loginResponse?.tokenApp
                        _currentUser.value = loginResponse?.user
                        _missatgeLogin.value = loginResponse?.message ?: "Login amb Google exitós"
                        loadSettings(context)
                    } else {
                        _missatgeLogin.value = "Error en iniciar sessió amb Google"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _missatgeLogin.value = "Error de connexió: ${e.message}"
                }
            }
        }
    }

    /**
     * Envía email para restablecer contraseña
     * @param email Email del usuario
     * @param context Contexto de la aplicación
     */
    fun sendResetPasswordEmail(email: String, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.resetPassword(email)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        android.widget.Toast.makeText(context, "Correu enviat si l'usuari existeix", android.widget.Toast.LENGTH_LONG).show()
                    } else {
                        android.widget.Toast.makeText(context, "No s'ha pogut enviar el correu", android.widget.Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "Error de connexió: ${e.localizedMessage}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Cierra la sesión actual
     * @param context Contexto de la aplicación
     */
    fun logout(context: Context) {
        _currentUser.value = null
        _token.value = null

        val googleClient = GoogleAuthUiClient(context)
        googleClient.signOut(context)
    }

    /**
     * Cierra todas las sesiones del usuario
     * @param context Contexto de la aplicación
     */
    fun logoutAll(context: Context) {
        val user = _currentUser.value

        if (user != null) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = repository.logoutApp(
                        email = user.email,
                        password = user.password,
                        googleId = user.google_id
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            _missatgeLogin.value = "Sessió tancada correctament."
                        } else {
                            _missatgeLogin.value = "Error al tancar sessió: ${response.body()?.get("message") ?: response.message()}"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        _missatgeLogin.value = "Error de connexió: ${e.message}"
                    }
                } finally {
                    withContext(Dispatchers.Main) {
                        _currentUser.value = null
                        _token.value = null

                        val googleClient = GoogleAuthUiClient(context)
                        googleClient.signOut(context)
                    }
                }
            }
        } else {
            _currentUser.value = null
            _token.value = null

            val googleClient = GoogleAuthUiClient(context)
            googleClient.signOut(context)
        }
    }

    /**
     * Carga los datos de la sesión actual
     */
    fun loadSession() {
        nicknameField.value  = currentUser.value?.nickname ?: "Nickname"
        avatarUrlField.value = currentUser.value?.avatar_url ?: "https://example.com/default_avatar.png"
        newPassword.value    = currentUser.value?.password ?: ""
    }

    /**
     * Actualiza el perfil del usuario
     * @param changePassword Indica si se debe actualizar la contraseña
     */
    fun updateProfile(changePassword: Boolean) {
        val t    = token.value ?: return
        val user = currentUser.value ?: return


        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = repository.updateUser(
                    token = t,
                    email = user.email,
                    nickname = nicknameField.value,
                    avatarUrl = avatarUrlField.value,
                    // mantenim els flags tal com estan
                    isAdmin = user.is_admin,
                    password = changePassword.let { if (it) newPassword.value else false },

                    isBanned = user.is_banned
                )

                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        // actualitzem l'usuari en memòria i enviem missatge OK
                        val updated = resp.body()!!.user
                        _currentUser.value = updated
                        _updateMsg.value    = resp.body()!!.message
                    } else {
                        _updateError.value  = "Error ${resp.code()} actualitzant perfil"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _updateError.value = "Connexió fallida: ${e.localizedMessage}"
                }
            }
        }
    }

    /**
     * Limpia los estados de actualización
     */
    fun clearUpdateState() {
        _updateMsg.value   = null
        _updateError.value = null
    }

    /**
     * Guarda el idioma seleccionado en preferencias
     * @param context Contexto de la aplicación
     * @param lang Código de idioma
     */
    fun saveLanguageToPrefs(context: Context, lang: String) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("lang", lang).apply()
    }

    /**
     * Verifica si el usuario ya ha elegido un idioma
     * @param context Contexto de la aplicación
     * @return true si el usuario ya eligió idioma, false en caso contrario
     */
    fun hasUserChosenLanguage(context: Context): Boolean {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return prefs.getBoolean("lang_selected", false)
    }

    /**
     * Obtiene el idioma guardado en preferencias
     * @param context Contexto de la aplicación
     * @return Código del idioma guardado
     */
    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("lang", null)

        return when (lang) {
            "ca", "es", "en" -> lang
            else -> "en"
        }
    }

    /**
     * Carga las configuraciones del usuario
     * @param context Contexto de la aplicación
     */
    fun loadSettings(context: Context) {
        val t = token.value ?: return
        val u = currentUser.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = repository.getUserSettings(t, u.email)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        resp.body()?.let { s ->
                            themeModeField.value             = s.theme_mode
                            langCodeField.value              = s.lang_code
                            allowNotificationField.value     = s.allow_notification
                            mergeScheduleCalendarField.value = s.merge_schedule_calendar

                            saveLanguageToPrefs(context, s.lang_code)
                            context.setLocale(s.lang_code)

                            isSettingsLoaded.value = true // ✅ Indiquem que s'han carregat correctament
                        }
                    } else {
                        _settingsError.value = "Error carregant settings: ${resp.code()}"
                        isSettingsLoaded.value = true // ✅ Tot i error, evitem bloqueig indefinit
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _settingsError.value = "Connexió fallida: ${e.localizedMessage}"
                    isSettingsLoaded.value = true // ✅ També aquí per evitar bloqueig
                }
            }
        }
    }

    /**
     * Actualiza las configuraciones del usuario
     * @param context Contexto de la aplicación
     */
    fun updateSettings(context: Context) {
        val t = token.value ?: return
        val u = currentUser.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = repository.updateUserSettings(
                    t,
                    u.email,
                    themeModeField.value,
                    langCodeField.value,
                    allowNotificationField.value,
                    mergeScheduleCalendarField.value
                )
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        _settingsMsg.value = resp.body()?.message
                        loadSettings(context)
                    } else {
                        _settingsError.value = "Error ${resp.code()} actualitzant settings"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _settingsError.value = "Connexió fallida: ${e.localizedMessage}"
                }
            }
        }
    }

    /**
     * Limpia los estados de configuración
     */
    fun clearSettingsState() {
        _settingsMsg.value   = null
        _settingsError.value = null
    }

    /**
     * Elimina la cuenta del usuario
     * @param context Contexto de la aplicación
     */
    fun deleteUser(context: Context){
        val t = token.value ?: return
        val u = currentUser.value ?: return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = repository.deleteUser(t, u.email)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        _updateMsg.value = resp.body()?.message
                    } else {
                        _updateError.value  = "Error ${resp.code()} eliminant perfil"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _updateError.value = "Connexió fallida: ${e.localizedMessage}"
                }
            }finally {
                logout(context)
            }
        }
    }

    /**
     * Envía un mensaje de contacto
     * @param email Email del usuario
     * @param message Mensaje a enviar
     */
    fun sendMessage(email: String, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val resp = repository.contactUs(email, message)
                withContext(Dispatchers.Main) {
                    if (resp.isSuccessful) {
                        _updateMsg.value = resp.body()?.message
                    } else {
                        _updateError.value  = "Error ${resp.code()} enviant missatge"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _updateError.value = "Connexió fallida: ${e.localizedMessage}"
                }
            }
        }
    }
}
