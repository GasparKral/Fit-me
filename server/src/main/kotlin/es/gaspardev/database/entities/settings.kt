package es.gaspardev.database.entities

import es.gaspardev.core.domain.dtos.settings.*
import es.gaspardev.database.*
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

// ============================================================================
// ENTIDADES DE CONFIGURACIONES
// ============================================================================

class AccountSettingsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AccountSettingsEntity>(AccountSettings)

    var user by UserEntity referencedOn AccountSettings.userId
    var emailNotifications by AccountSettings.emailNotifications
    var smsNotifications by AccountSettings.smsNotifications
    var twoFactorEnabled by AccountSettings.twoFactorEnabled
    var sessionTimeout by AccountSettings.sessionTimeout
    var autoLogout by AccountSettings.autoLogout
    var createdAt by AccountSettings.createdAt
    var updatedAt by AccountSettings.updatedAt

    fun toModel(): AccountSettingsData {
        return AccountSettingsData(
            currentPassword = "", // No devolvemos la contraseña actual por seguridad
            newPassword = null,
            confirmNewPassword = null,
            emailNotifications = this.emailNotifications,
            smsNotifications = this.smsNotifications,
            twoFactorEnabled = this.twoFactorEnabled,
            sessionTimeout = this.sessionTimeout,
            autoLogout = this.autoLogout
        )
    }
}

class AppearanceSettingsEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AppearanceSettingsEntity>(AppearanceSettings)

    var user by UserEntity referencedOn AppearanceSettings.userId
    var theme by AppearanceSettings.theme
    var createdAt by AppearanceSettings.createdAt
    var updatedAt by AppearanceSettings.updatedAt

    fun toModel(): AppearanceSettingsData {
        return AppearanceSettingsData(
            theme = this.theme.name.lowercase(),
        )
    }
}
