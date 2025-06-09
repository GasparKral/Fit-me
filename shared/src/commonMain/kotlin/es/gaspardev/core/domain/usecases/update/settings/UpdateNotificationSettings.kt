package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.NotificationSettingsData
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para actualizar la configuración de notificaciones del usuario
 * 
 * @param repository Repositorio de configuraciones
 */
class UpdateNotificationSettings(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, Pair<User, NotificationSettingsData>>() {

    /**
     * Ejecuta la actualización de la configuración de notificaciones
     * 
     * @param params Par que contiene el usuario y los datos de notificaciones a actualizar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Pair<User, NotificationSettingsData>): Either<Exception, UserSettings> {
        val (user, notificationData) = params
        
        // Validar datos de notificaciones antes de enviar al repositorio
        val validationResult = validateNotificationData(notificationData)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }
        
        return repository.updateNotificationSettings(user, notificationData)
    }

    /**
     * Valida los datos de notificaciones antes de procesar la actualización
     * 
     * @param notificationData Datos de notificaciones a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateNotificationData(notificationData: NotificationSettingsData): Exception? {
        // Validar hora de notificación (formato HH:mm)
        notificationData.notificationTime?.let { time ->
            if (time.isNotBlank() && !isValidTimeFormat(time)) {
                return IllegalArgumentException("La hora de notificación debe tener el formato HH:mm (ej: 09:30)")
            }
        }
        
        // Validar horas de silencio
        val quietStart = notificationData.quietHoursStart
        val quietEnd = notificationData.quietHoursEnd
        
        if (quietStart != null && quietStart.isNotBlank()) {
            if (!isValidTimeFormat(quietStart)) {
                return IllegalArgumentException("La hora de inicio del modo silencioso debe tener el formato HH:mm")
            }
        }
        
        if (quietEnd != null && quietEnd.isNotBlank()) {
            if (!isValidTimeFormat(quietEnd)) {
                return IllegalArgumentException("La hora de fin del modo silencioso debe tener el formato HH:mm")
            }
        }
        
        // Validar que ambas horas de silencio estén definidas si una está presente
        if ((quietStart.isNullOrBlank() && !quietEnd.isNullOrBlank()) ||
            (!quietStart.isNullOrBlank() && quietEnd.isNullOrBlank())) {
            return IllegalArgumentException("Debe especificar tanto la hora de inicio como la de fin para el modo silencioso")
        }
        
        // Validar que las horas de silencio sean diferentes
        if (!quietStart.isNullOrBlank() && !quietEnd.isNullOrBlank() && quietStart == quietEnd) {
            return IllegalArgumentException("La hora de inicio y fin del modo silencioso deben ser diferentes")
        }
        
        // Validar dependencias lógicas
        val logicalValidation = validateLogicalDependencies(notificationData)
        if (logicalValidation != null) {
            return logicalValidation
        }
        
        return null
    }
    
    /**
     * Valida que el formato de tiempo sea correcto (HH:mm)
     */
    private fun isValidTimeFormat(time: String): Boolean {
        val timeRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
        return timeRegex.matches(time)
    }
    
    /**
     * Valida las dependencias lógicas entre configuraciones
     */
    private fun validateLogicalDependencies(notificationData: NotificationSettingsData): Exception? {
        // Si las notificaciones push están deshabilitadas, verificar consistencia
        if (!notificationData.pushNotifications) {
            // Advertir sobre configuraciones que dependen de push notifications
            if (notificationData.workoutReminders || 
                notificationData.sessionReminders || 
                notificationData.messageNotifications) {
                // Esto no es un error, pero podría ser una advertencia para el usuario
                // Por ahora, permitimos esta configuración
            }
        }
        
        // Si las notificaciones de email están deshabilitadas
        if (!notificationData.emailNotifications) {
            if (notificationData.progressReports || notificationData.marketingEmails) {
                // Similar al caso anterior, permitimos pero podría ser inconsistente
            }
        }
        
        // Si tanto sonido como vibración están deshabilitados, las notificaciones push podrían ser menos efectivas
        if (notificationData.pushNotifications && !notificationData.sound && !notificationData.vibration) {
            // Esta es una configuración válida pero el usuario podría perderse las notificaciones
            // No generamos error, es una preferencia del usuario
        }
        
        return null
    }
    
    companion object {
        // Horas de notificación recomendadas
        val RECOMMENDED_NOTIFICATION_TIMES = listOf(
            "08:00", "09:00", "10:00", "18:00", "19:00", "20:00"
        )
        
        // Configuraciones predefinidas de horas de silencio
        val PRESET_QUIET_HOURS = mapOf(
            "night" to Pair("22:00", "07:00"),
            "work" to Pair("09:00", "17:00"),
            "custom" to Pair(null, null)
        )
        
        /**
         * Obtiene la configuración por defecto de notificaciones
         */
        fun getDefaultNotificationSettings(): NotificationSettingsData {
            return NotificationSettingsData(
                pushNotifications = true,
                emailNotifications = true,
                smsNotifications = false,
                workoutReminders = true,
                sessionReminders = true,
                messageNotifications = true,
                progressReports = true,
                marketingEmails = false,
                sound = true,
                vibration = true,
                notificationTime = "09:00",
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }
        
        /**
         * Obtiene una configuración mínima de notificaciones (solo lo esencial)
         */
        fun getMinimalNotificationSettings(): NotificationSettingsData {
            return NotificationSettingsData(
                pushNotifications = true,
                emailNotifications = false,
                smsNotifications = false,
                workoutReminders = false,
                sessionReminders = true,
                messageNotifications = true,
                progressReports = false,
                marketingEmails = false,
                sound = false,
                vibration = true,
                notificationTime = null,
                quietHoursStart = null,
                quietHoursEnd = null
            )
        }
    }
}
