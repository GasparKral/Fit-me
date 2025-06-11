package es.gaspardev.database.daos

import es.gaspardev.core.domain.dtos.settings.*
import es.gaspardev.database.entities.*
import es.gaspardev.enums.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.transaction

object SettingsDao {

    // ============================================================================
    // ACCOUNT SETTINGS
    // ============================================================================

    fun getAccountSettings(userId: Int): AccountSettingsData? = transaction {
        AccountSettingsEntity.find {
            es.gaspardev.database.AccountSettings.userId eq userId
        }.singleOrNull()?.toModel()
    }

    fun updateAccountSettings(userId: Int, settings: AccountSettingsData): AccountSettingsEntity = transaction {
        val existing = AccountSettingsEntity.find {
            es.gaspardev.database.AccountSettings.userId eq userId
        }.singleOrNull()

        if (existing != null) {
            existing.apply {
                emailNotifications = settings.emailNotifications
                smsNotifications = settings.smsNotifications
                twoFactorEnabled = settings.twoFactorEnabled
                sessionTimeout = settings.sessionTimeout
                autoLogout = settings.autoLogout
                updatedAt = Clock.System.now()
            }
        } else {
            AccountSettingsEntity.new {
                user = UserEntity[userId]
                emailNotifications = settings.emailNotifications
                smsNotifications = settings.smsNotifications
                twoFactorEnabled = settings.twoFactorEnabled
                sessionTimeout = settings.sessionTimeout
                autoLogout = settings.autoLogout
                createdAt = Clock.System.now()
                updatedAt = Clock.System.now()
            }
        }
    }

    // ============================================================================
    // APPEARANCE SETTINGS
    // ============================================================================

    fun getAppearanceSettings(userId: Int): AppearanceSettingsData? = transaction {
        AppearanceSettingsEntity.find {
            es.gaspardev.database.AppearanceSettings.userId eq userId
        }.singleOrNull()?.toModel()
    }

    fun updateAppearanceSettings(userId: Int, settings: AppearanceSettingsData): AppearanceSettingsEntity =
        transaction {
            val existing = AppearanceSettingsEntity.find {
                es.gaspardev.database.AppearanceSettings.userId eq userId
            }.singleOrNull()

            if (existing != null) {
                existing.apply {
                    theme = ThemeType.valueOf(settings.theme.uppercase())
                    updatedAt = Clock.System.now()
                }
            } else {
                AppearanceSettingsEntity.new {
                    user = UserEntity[userId]
                    theme = ThemeType.valueOf(settings.theme.uppercase())
                    createdAt = Clock.System.now()
                    updatedAt = Clock.System.now()
                }
            }
        }

    // ============================================================================
    // NOTIFICATION SETTINGS
    // ============================================================================


    // ============================================================================
    // PROFILE SETTINGS (usando datos existentes del usuario)
    // ============================================================================

    fun getProfileSettings(userId: Int): ProfileSettingsData? = transaction {
        val user = UserEntity.findById(userId) ?: return@transaction null

        // Si es entrenador, incluir información adicional
        val trainer = TrainerEntity.find { es.gaspardev.database.Trainers.userId eq userId }.singleOrNull()

        ProfileSettingsData(
            fullName = user.fullname,
            email = user.email,
            phone = user.phone,
            specialization = trainer?.specialization,
            yearsOfExperience = trainer?.yearsOfExperience
        )
    }

    fun updateProfileSettings(userId: Int, settings: ProfileSettingsData): UserEntity = transaction {
        val user = UserEntity[userId]

        user.apply {
            fullname = settings.fullName
            email = settings.email
            phone = settings.phone ?: ""
        }

        // Si es entrenador, actualizar información específica
        val trainer = TrainerEntity.find { es.gaspardev.database.Trainers.userId eq userId }.singleOrNull()
        trainer?.apply {
            specialization = settings.specialization ?: ""
            yearsOfExperience = settings.yearsOfExperience ?: 0
        }

        user
    }

    // ============================================================================
    // OPERACIONES COMBINADAS
    // ============================================================================

    /**
     * Obtiene todas las configuraciones de un usuario
     */
    fun getAllUserSettings(userId: Int): Map<String, Any?> = transaction {
        mapOf(
            "account" to getAccountSettings(userId),
            "appearance" to getAppearanceSettings(userId),
            "profile" to getProfileSettings(userId)
        )
    }

    /**
     * Crea configuraciones por defecto para un nuevo usuario
     */
    fun createDefaultSettings(userId: Int) = transaction {
        // Account settings por defecto
        if (AccountSettingsEntity.find { es.gaspardev.database.AccountSettings.userId eq userId }.empty()) {
            AccountSettingsEntity.new {
                user = UserEntity[userId]
                emailNotifications = true
                smsNotifications = false
                twoFactorEnabled = false
                sessionTimeout = 60
                autoLogout = false
                createdAt = Clock.System.now()
                updatedAt = Clock.System.now()
            }
        }

        // Appearance settings por defecto
        if (AppearanceSettingsEntity.find { es.gaspardev.database.AppearanceSettings.userId eq userId }.empty()) {
            AppearanceSettingsEntity.new {
                user = UserEntity[userId]
                theme = ThemeType.SYSTEM
                createdAt = Clock.System.now()
                updatedAt = Clock.System.now()
            }
        }

    }
}


/**
 * Obtiene estadísticas de configuraciones
 */
fun getSettingsStats(): Map<String, Any> = transaction {
    val totalUsers = UserEntity.all().count()
    val usersWithTwoFactor = AccountSettingsEntity.find {
        es.gaspardev.database.AccountSettings.twoFactorEnabled eq true
    }.count()
    val darkThemeUsers = AppearanceSettingsEntity.find {
        es.gaspardev.database.AppearanceSettings.theme eq ThemeType.DARK
    }.count()

    mapOf(
        "totalUsers" to totalUsers,
        "twoFactorEnabled" to usersWithTwoFactor,
        "darkThemeUsers" to darkThemeUsers,
        "twoFactorPercentage" to if (totalUsers > 0) (usersWithTwoFactor * 100 / totalUsers) else 0,
        "darkThemePercentage" to if (totalUsers > 0) (darkThemeUsers * 100 / totalUsers) else 0
    )
}

