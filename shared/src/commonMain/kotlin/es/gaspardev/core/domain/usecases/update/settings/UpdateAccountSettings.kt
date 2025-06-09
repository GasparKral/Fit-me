package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.AccountSettingsData
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para actualizar la configuración de cuenta del usuario
 * 
 * @param repository Repositorio de configuraciones
 */
class UpdateAccountSettings(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, Pair<User, AccountSettingsData>>() {

    /**
     * Ejecuta la actualización de la configuración de cuenta
     * 
     * @param params Par que contiene el usuario y los datos de cuenta a actualizar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Pair<User, AccountSettingsData>): Either<Exception, UserSettings> {
        val (user, accountData) = params
        
        // Validar datos de cuenta antes de enviar al repositorio
        val validationResult = validateAccountData(accountData)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        // Si hay cambio de contraseña, procesarlo por separado
        if (shouldChangePassword(accountData)) {
            val passwordResult = changePassword(user, accountData)
            if (passwordResult is Either.Failure) {
                return passwordResult
            }
        }
        
        return repository.updateAccountSettings(user, accountData)
    }

    /**
     * Valida los datos de cuenta antes de procesar la actualización
     * 
     * @param accountData Datos de cuenta a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateAccountData(accountData: AccountSettingsData): Exception? {
        // Validar contraseña actual (siempre obligatoria para cambios de seguridad)
        if (accountData.currentPassword.isBlank()) {
            return IllegalArgumentException("La contraseña actual es obligatoria para realizar cambios de seguridad")
        }
        
        // Si se va a cambiar la contraseña, validar los nuevos datos
        if (shouldChangePassword(accountData)) {
            val passwordValidation = validatePasswordChange(accountData)
            if (passwordValidation != null) {
                return passwordValidation
            }
        }
        
        // Validar timeout de sesión (debe estar en un rango válido)
        if (accountData.sessionTimeout < 5) {
            return IllegalArgumentException("El timeout de sesión debe ser de al menos 5 minutos")
        }
        
        if (accountData.sessionTimeout > 10080) { // 7 días en minutos
            return IllegalArgumentException("El timeout de sesión no puede exceder 7 días")
        }
        
        return null
    }
    
    /**
     * Determina si se debe cambiar la contraseña basándose en los datos proporcionados
     */
    private fun shouldChangePassword(accountData: AccountSettingsData): Boolean {
        return !accountData.newPassword.isNullOrBlank() && !accountData.confirmNewPassword.isNullOrBlank()
    }
    
    /**
     * Valida los datos para el cambio de contraseña
     */
    private fun validatePasswordChange(accountData: AccountSettingsData): Exception? {
        val newPassword = accountData.newPassword
        val confirmPassword = accountData.confirmNewPassword
        
        if (newPassword.isNullOrBlank()) {
            return IllegalArgumentException("La nueva contraseña no puede estar vacía")
        }
        
        if (confirmPassword.isNullOrBlank()) {
            return IllegalArgumentException("La confirmación de contraseña no puede estar vacía")
        }
        
        if (newPassword != confirmPassword) {
            return IllegalArgumentException("Las contraseñas no coinciden")
        }
        
        // Validar fortaleza de la contraseña
        if (newPassword.length < 8) {
            return IllegalArgumentException("La contraseña debe tener al menos 8 caracteres")
        }
        
        if (newPassword.length > 128) {
            return IllegalArgumentException("La contraseña no puede exceder 128 caracteres")
        }
        
        if (!hasRequiredPasswordComplexity(newPassword)) {
            return IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula, una minúscula, un número y un carácter especial")
        }
        
        // Verificar que la nueva contraseña sea diferente a la actual
        if (newPassword == accountData.currentPassword) {
            return IllegalArgumentException("La nueva contraseña debe ser diferente a la actual")
        }
        
        return null
    }
    
    /**
     * Verifica que la contraseña cumpla con los requisitos de complejidad
     */
    private fun hasRequiredPasswordComplexity(password: String): Boolean {
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }
        
        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar
    }
    
    /**
     * Procesa el cambio de contraseña
     */
    private suspend fun changePassword(user: User, accountData: AccountSettingsData): Either<Exception, Unit> {
        return repository.changePassword(
            user = user,
            currentPassword = accountData.currentPassword,
            newPassword = accountData.newPassword!!
        )
    }
}
