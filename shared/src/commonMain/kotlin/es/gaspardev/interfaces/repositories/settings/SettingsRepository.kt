package es.gaspardev.interfaces.repositories.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.*
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.infrastructure.apis.SettingsAPI

interface SettingsRepository {

    companion object {
        val API = SettingsAPI()
    }

    suspend fun getUserSettings(user: User): Either<Exception, UserSettings>
    
    suspend fun updateProfileSettings(
        user: User, 
        profileData: ProfileSettingsData
    ): Either<Exception, UserSettings>
    
    suspend fun updateAccountSettings(
        user: User, 
        accountData: AccountSettingsData
    ): Either<Exception, UserSettings>
    
    suspend fun updateAppearanceSettings(
        user: User, 
        appearanceData: AppearanceSettingsData
    ): Either<Exception, UserSettings>
    
    suspend fun updateNotificationSettings(
        user: User, 
        notificationData: NotificationSettingsData
    ): Either<Exception, UserSettings>
    
    suspend fun changePassword(
        user: User, 
        currentPassword: String, 
        newPassword: String
    ): Either<Exception, Unit>
    
    suspend fun updateProfileImage(
        user: User, 
        imageUrl: String
    ): Either<Exception, UserSettings>
    
    suspend fun resetToDefaults(
        user: User, 
        settingsType: String
    ): Either<Exception, UserSettings>
}
