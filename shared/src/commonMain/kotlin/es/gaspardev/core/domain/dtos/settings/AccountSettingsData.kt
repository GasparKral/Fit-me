package es.gaspardev.core.domain.dtos.settings

import kotlinx.serialization.Serializable

@Serializable
data class AccountSettingsData(
    val currentPassword: String,
    val newPassword: String?,
    val confirmNewPassword: String?,
    val emailNotifications: Boolean,
    val smsNotifications: Boolean,
    val twoFactorEnabled: Boolean,
    val sessionTimeout: Int, // en minutos
    val autoLogout: Boolean
)
