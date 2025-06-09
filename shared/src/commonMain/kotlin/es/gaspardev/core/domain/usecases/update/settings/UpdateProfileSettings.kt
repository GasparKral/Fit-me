package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.ProfileSettingsData
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para actualizar la configuración del perfil del usuario
 * 
 * @param repository Repositorio de configuraciones
 */
class UpdateProfileSettings(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, Pair<User, ProfileSettingsData>>() {

    /**
     * Ejecuta la actualización de la configuración del perfil
     * 
     * @param params Par que contiene el usuario y los datos del perfil a actualizar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Pair<User, ProfileSettingsData>): Either<Exception, UserSettings> {
        val (user, profileData) = params
        
        // Validar datos del perfil antes de enviar al repositorio
        val validationResult = validateProfileData(profileData)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repository.updateProfileSettings(user, profileData)
    }

    /**
     * Valida los datos del perfil antes de procesar la actualización
     * 
     * @param profileData Datos del perfil a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateProfileData(profileData: ProfileSettingsData): Exception? {
        // Validar nombre completo (obligatorio)
        if (profileData.fullName.isBlank()) {
            return IllegalArgumentException("El nombre completo es obligatorio")
        }
        
        if (profileData.fullName.length < 2) {
            return IllegalArgumentException("El nombre debe tener al menos 2 caracteres")
        }
        
        if (profileData.fullName.length > 255) {
            return IllegalArgumentException("El nombre no puede exceder 255 caracteres")
        }
        
        // Validar email (obligatorio y formato válido)
        if (profileData.email.isBlank()) {
            return IllegalArgumentException("El email es obligatorio")
        }
        
        if (!isValidEmail(profileData.email)) {
            return IllegalArgumentException("El formato del email no es válido")
        }
        
        // Validar teléfono (opcional, pero si se proporciona debe ser válido)
        profileData.phone?.let { phone ->
            if (phone.isNotBlank() && !isValidPhone(phone)) {
                return IllegalArgumentException("El formato del teléfono no es válido")
            }
        }
        
        // Validar años de experiencia (debe ser positivo si se proporciona)
        profileData.yearsOfExperience?.let { years ->
            if (years < 0) {
                return IllegalArgumentException("Los años de experiencia no pueden ser negativos")
            }
            if (years > 100) {
                return IllegalArgumentException("Los años de experiencia no pueden exceder 100")
            }
        }
        
        // Validar especialización (opcional, pero con límite de caracteres)
        profileData.specialization?.let { specialization ->
            if (specialization.length > 500) {
                return IllegalArgumentException("La especialización no puede exceder 500 caracteres")
            }
        }
        
        // Validar biografía (opcional, pero con límite de caracteres)
        profileData.bio?.let { bio ->
            if (bio.length > 1000) {
                return IllegalArgumentException("La biografía no puede exceder 1000 caracteres")
            }
        }
        
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
     * Valida el formato del teléfono (permite varios formatos internacionales)
     */
    private fun isValidPhone(phone: String): Boolean {
        // Regex básico para teléfonos (permite +, -, espacios, paréntesis y números)
        val phoneRegex = Regex("^[+]?[0-9\\s\\-\\(\\)]{7,20}$")
        return phoneRegex.matches(phone)
    }
}
