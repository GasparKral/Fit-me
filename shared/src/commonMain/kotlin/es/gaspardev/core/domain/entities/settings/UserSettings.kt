package es.gaspardev.core.domain.entities.settings

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class UserSettings(
    val userId: Int,
    val profileSettings: ProfileSettings,
    val accountSettings: AccountSettings,
    val appearanceSettings: AppearanceSettings,
    val notificationSettings: NotificationSettings,
    val lastUpdated: Instant
)

@Serializable
data class ProfileSettings(
    val fullName: String,
    val email: String,
    val phone: String?,
    val specialization: String?,
    val yearsOfExperience: Int?,
    val bio: String?,
    val profileImageUrl: String?
)

@Serializable
data class AccountSettings(
    val emailNotifications: Boolean,
    val smsNotifications: Boolean,
    val twoFactorEnabled: Boolean,
    val sessionTimeout: Int,
    val autoLogout: Boolean,
    val lastPasswordChange: Instant?
)

@Serializable
data class AppearanceSettings(
    val theme: String,
    val accentColor: String,
    val fontSize: String,
    val compactMode: Boolean,
    val showSidebar: Boolean,
    val animationsEnabled: Boolean,
    val language: String
)

@Serializable
data class NotificationSettings(
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
    val notificationTime: String?,
    val quietHoursStart: String?,
    val quietHoursEnd: String?
)
