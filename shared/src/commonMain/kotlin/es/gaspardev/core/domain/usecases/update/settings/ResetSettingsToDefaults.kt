package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para restablecer configuraciones a valores por defecto
 * 
 * @param repository Repositorio de configuraciones
 */
class ResetSettingsToDefaults(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, ResetSettingsParams>() {

    /**
     * Ejecuta el restablecimiento de configuraciones
     * 
     * @param params Parámetros para el restablecimiento
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: ResetSettingsParams): Either<Exception, UserSettings> {
        // Validar tipo de configuración
        val validationResult = validateResetParams(params)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repository.resetToDefaults(
            user = params.user,
            settingsType = params.settingsType
        )
    }

    /**
     * Valida los parámetros para el restablecimiento
     * 
     * @param params Parámetros a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateResetParams(params: ResetSettingsParams): Exception? {
        // Validar que el tipo de configuración sea válido
        if (!isValidSettingsType(params.settingsType)) {
            return IllegalArgumentException("Tipo de configuración no válido: ${params.settingsType}")
        }
        
        return null
    }
    
    /**
     * Verifica que el tipo de configuración sea uno de los soportados
     */
    private fun isValidSettingsType(settingsType: String): Boolean {
        return settingsType in SUPPORTED_SETTINGS_TYPES
    }
    
    companion object {
        // Tipos de configuración que se pueden restablecer
        val SUPPORTED_SETTINGS_TYPES = listOf(
            "profile",      // Configuración de perfil
            "account",      // Configuración de cuenta
            "appearance",   // Configuración de apariencia
            "notifications", // Configuración de notificaciones
            "all"          // Todas las configuraciones
        )
        
        /**
         * Obtiene la descripción de cada tipo de configuración
         */
        fun getSettingsTypeDescription(settingsType: String): String {
            return when (settingsType) {
                "profile" -> "Configuración de perfil (nombre, email, especialización, biografía)"
                "account" -> "Configuración de cuenta (notificaciones por email/SMS, 2FA, timeout de sesión)"
                "appearance" -> "Configuración de apariencia (tema, color, fuente, idioma)"
                "notifications" -> "Configuración de notificaciones (push, recordatorios, horas de silencio)"
                "all" -> "Todas las configuraciones"
                else -> "Tipo de configuración desconocido"
            }
        }
        
        /**
         * Obtiene un mensaje de confirmación para el tipo de restablecimiento
         */
        fun getConfirmationMessage(settingsType: String): String {
            return when (settingsType) {
                "profile" -> "¿Estás seguro de que quieres restablecer la configuración de perfil? Se perderán los cambios personalizados en tu información personal."
                "account" -> "¿Estás seguro de que quieres restablecer la configuración de cuenta? Se perderán las preferencias de seguridad y notificaciones."
                "appearance" -> "¿Estás seguro de que quieres restablecer la configuración de apariencia? Se volverá al tema y configuración visual por defecto."
                "notifications" -> "¿Estás seguro de que quieres restablecer la configuración de notificaciones? Se perderán todas las preferencias de notificaciones personalizadas."
                "all" -> "¿Estás seguro de que quieres restablecer TODAS las configuraciones? Esta acción no se puede deshacer y se perderán todas las personalizaciones."
                else -> "¿Estás seguro de que quieres restablecer esta configuración?"
            }
        }
        
        /**
         * Determina si se requiere confirmación adicional para el tipo de restablecimiento
         */
        fun requiresStrongConfirmation(settingsType: String): Boolean {
            return settingsType == "all" || settingsType == "account"
        }
    }
}

/**
 * Parámetros para el restablecimiento de configuraciones
 */
data class ResetSettingsParams(
    val user: User,
    val settingsType: String
)
