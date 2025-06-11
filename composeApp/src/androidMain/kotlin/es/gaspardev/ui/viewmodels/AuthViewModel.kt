package es.gaspardev.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.dtos.RegisterAthleteData
import es.gaspardev.core.domain.usecases.create.user.ResgisterNewAthlete
import es.gaspardev.core.domain.usecases.read.user.LogInUser
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.utils.encrypt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val trainerRepository = TrainerRepositoryImp()
    private val athleteRepository = AthletetRepositoryImp()

    /**
     * Maneja el login de usuarios (entrenadores)
     */
    fun login(userIdentification: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val credentials = LoginCredentials(
                    userIdetification = userIdentification,
                    userPassword = encrypt(password)
                )

                LogInUser(trainerRepository).run(credentials).fold(
                    onSuccess = { result ->
                        Log.d(TAG, "Login successful for trainer: ${result.first.user.fullname}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            currentTrainer = result.first,
                            athletes = result.second,
                            conversations = result.third
                        )
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Login failed", error)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error desconocido al iniciar sesión"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during login", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Maneja el registro de atletas usando un token de invitación
     */
    fun registerAthlete(
        name: String,
        email: String,
        age: Int,
        password: String,
        registrationToken: String?
    ) {
        if (registrationToken.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Token de registro requerido"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // Validar datos básicos
                val validationError = validateRegistrationData(name, email, age, password)
                if (validationError != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = validationError
                    )
                    return@launch
                }

                // Extraer trainer ID del token (esto dependerá de cómo implementes la validación del token)
                val trainerId = extractTrainerIdFromToken(registrationToken)
                if (trainerId == null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Token de registro inválido"
                    )
                    return@launch
                }

                val registerData = RegisterAthleteData(
                    userName = name,
                    password = password,
                    email = email,
                    sex = true,
                    age = age,
                    trainerId = trainerId
                )

                ResgisterNewAthlete(athleteRepository).run(registerData).fold(
                    onSuccess = { athlete ->
                        Log.d(TAG, "Registration successful for athlete: ${athlete.user.fullname}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            registrationComplete = true,
                            registeredAthlete = athlete
                        )
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Registration failed", error)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = error.message ?: "Error desconocido al registrarse"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error during registration", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error inesperado: ${e.message}"
                )
            }
        }
    }

    /**
     * Valida los datos de registro antes de enviarlos al servidor
     */
    private fun validateRegistrationData(
        name: String,
        email: String,
        age: Int,
        password: String
    ): String? {
        if (name.isBlank()) return "El nombre es obligatorio"
        if (name.length < 2) return "El nombre debe tener al menos 2 caracteres"

        if (email.isBlank()) return "El email es obligatorio"
        if (!isValidEmail(email)) return "El formato del email no es válido"

        if (age < 16) return "Debes tener al menos 16 años"
        if (age > 100) return "Edad no válida"

        if (password.isBlank()) return "La contraseña es obligatoria"
        if (password.length < 6) return "La contraseña debe tener al menos 6 caracteres"

        return null
    }

    /**
     * Valida el formato del email
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }

    /**
     * Extrae el trainer ID del token de registro
     * NOTA: Esto es una implementación de ejemplo.
     * Deberías implementar la lógica real según tu sistema de tokens.
     */
    private fun extractTrainerIdFromToken(token: String): Int? {
        return try {
            // Ejemplo: si el token contiene el trainer ID de alguna manera
            // Esto debería ser una llamada al servidor para validar el token
            // y obtener el trainer ID correspondiente

            // Por ahora, como ejemplo, asumimos que podemos extraer el ID
            // En la implementación real, harías una llamada al servidor
            Log.d(TAG, "Extracting trainer ID from token: $token")

            // Implementación temporal - CAMBIAR por validación real del servidor
            1 // Retorna un ID de ejemplo
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting trainer ID from token", e)
            null
        }
    }

    /**
     * Limpia el estado de error
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    /**
     * Resetea el estado de registro completado
     */
    fun resetRegistrationState() {
        _uiState.value = _uiState.value.copy(
            registrationComplete = false,
            registeredAthlete = null
        )
    }

    /**
     * Cierra sesión
     */
    fun logout() {
        _uiState.value = AuthUiState() // Resetea al estado inicial
    }
}

/**
 * Estado del UI de autenticación
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val registrationComplete: Boolean = false,

    // Datos del trainer logueado
    val currentTrainer: es.gaspardev.core.domain.entities.users.Trainer? = null,
    val athletes: List<es.gaspardev.core.domain.entities.users.Athlete> = emptyList(),
    val conversations: List<es.gaspardev.core.domain.entities.comunication.Conversation> = emptyList(),

    // Datos del atleta registrado
    val registeredAthlete: es.gaspardev.core.domain.entities.users.Athlete? = null
)