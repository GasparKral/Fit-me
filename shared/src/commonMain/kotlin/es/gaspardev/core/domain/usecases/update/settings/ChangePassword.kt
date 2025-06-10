package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository
import es.gaspardev.utils.encrypt

/**
 * Use case para cambiar la contraseña del usuario
 *
 * @param repository Repositorio de configuraciones
 */
class ChangePassword(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UseCase.None, ChangePasswordParams>() {

    /**
     * Ejecuta el cambio de contraseña
     *
     * @param params Parámetros para el cambio de contraseña
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: ChangePasswordParams): Either<Exception, None> {
        // Validar datos antes de enviar al repositorio
        val validationResult = validatePasswordChange(params)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }

        return repository.changePassword(
            user = params.user,
            currentPassword = encrypt(params.currentPassword),
            newPassword = encrypt(params.newPassword)
        ).map { None }
    }

    /**
     * Valida los datos para el cambio de contraseña
     *
     * @param params Parámetros a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validatePasswordChange(params: ChangePasswordParams): Exception? {
        // Validar contraseña actual
        if (params.currentPassword.isBlank()) {
            return IllegalArgumentException("La contraseña actual es obligatoria")
        }

        // Validar nueva contraseña
        if (params.newPassword.isBlank()) {
            return IllegalArgumentException("La nueva contraseña no puede estar vacía")
        }

        // Validar confirmación de contraseña
        if (params.confirmNewPassword.isBlank()) {
            return IllegalArgumentException("La confirmación de contraseña no puede estar vacía")
        }

        // Verificar que las contraseñas coincidan
        if (params.newPassword != params.confirmNewPassword) {
            return IllegalArgumentException("Las contraseñas no coinciden")
        }

        // Validar fortaleza de la contraseña
        val strengthValidation = validatePasswordStrength(params.newPassword)
        if (strengthValidation != null) {
            return strengthValidation
        }

        // Verificar que la nueva contraseña sea diferente a la actual
        if (params.newPassword == params.currentPassword) {
            return IllegalArgumentException("La nueva contraseña debe ser diferente a la actual")
        }

        return null
    }

    /**
     * Valida la fortaleza de la contraseña
     */
    private fun validatePasswordStrength(password: String): Exception? {
        if (password.length < 8) {
            return IllegalArgumentException("La contraseña debe tener al menos 8 caracteres")
        }

        if (password.length > 128) {
            return IllegalArgumentException("La contraseña no puede exceder 128 caracteres")
        }

        if (!hasRequiredPasswordComplexity(password)) {
            return IllegalArgumentException("La contraseña debe contener al menos una letra mayúscula, una minúscula, un número y un carácter especial")
        }

        // Verificar que no sea una contraseña común
        if (isCommonPassword(password)) {
            return IllegalArgumentException("Esta contraseña es muy común. Por favor, elige una contraseña más segura")
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
     * Verifica si la contraseña está en la lista de contraseñas comunes
     */
    private fun isCommonPassword(password: String): Boolean {
        val commonPasswords = listOf(
            "password", "123456", "password123", "admin", "qwerty",
            "letmein", "welcome", "monkey", "dragon", "master",
            "password1", "123456789", "12345678", "qwertyuiop"
        )
        return commonPasswords.contains(password.lowercase())
    }
}

/**
 * Parámetros para el cambio de contraseña
 */
data class ChangePasswordParams(
    val user: User,
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)
