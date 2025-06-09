package es.gaspardev.core.infrastructure.repositories.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.*
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

class SettingsRepositoryImp : SettingsRepository {

    override suspend fun getUserSettings(user: User): Either<Exception, UserSettings> {
        return SettingsRepository.API.getSingleValue<UserSettings>(
            segments = listOf(user.id.toString())
        )
    }

    override suspend fun updateProfileSettings(
        user: User,
        profileData: ProfileSettingsData
    ): Either<Exception, UserSettings> {
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "profile",
            settingsData = profileData
        )
    }

    override suspend fun updateAccountSettings(
        user: User,
        accountData: AccountSettingsData
    ): Either<Exception, UserSettings> {
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "account", 
            settingsData = accountData
        )
    }

    override suspend fun updateAppearanceSettings(
        user: User,
        appearanceData: AppearanceSettingsData
    ): Either<Exception, UserSettings> {
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "appearance",
            settingsData = appearanceData
        )
    }

    override suspend fun updateNotificationSettings(
        user: User,
        notificationData: NotificationSettingsData
    ): Either<Exception, UserSettings> {
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "notifications",
            settingsData = notificationData
        )
    }

    override suspend fun changePassword(
        user: User,
        currentPassword: String,
        newPassword: String
    ): Either<Exception, Unit> {
        val passwordData = mapOf(
            "currentPassword" to currentPassword,
            "newPassword" to newPassword
        )
        
        return SettingsRepository.API.changePassword(
            userId = user.id,
            passwordData = passwordData
        )
    }

    override suspend fun updateProfileImage(
        user: User,
        imageUrl: String
    ): Either<Exception, UserSettings> {
        val imageData = mapOf("profileImageUrl" to imageUrl)
        
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "profile-image",
            settingsData = imageData
        )
    }

    override suspend fun resetToDefaults(
        user: User,
        settingsType: String
    ): Either<Exception, UserSettings> {
        return SettingsRepository.API.updateSettings(
            userId = user.id,
            settingsType = "reset-$settingsType",
            settingsData = emptyMap<String, Any>()
        )
    }
}
