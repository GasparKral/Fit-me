package es.gaspardev.core.domain.dtos.settings

import kotlinx.serialization.Serializable

@Serializable
data class NotificationSettingsData(
    val pushNotifications: Boolean,
    val emailNotifications: Boolean,
    val smsNotifications: Boolean,
    val workoutReminders: Boolean,
    val sessionReminders: Boolean,
    val messageNotifications: Boolean,
    val progressReports: Boolean,
    val marketingEmails: Boolean,
    val sound: Boolean,
    val vibration: Boolean,
    val notificationTime: String?, // formato HH:mm
    val quietHoursStart: String?, // formato HH:mm
    val quietHoursEnd: String? // formato HH:mm
)
