package es.gaspardev.core.domain.usecases.update.settings

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.settings.AppearanceSettingsData
import es.gaspardev.core.domain.entities.settings.UserSettings
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.settings.SettingsRepositoryImp
import es.gaspardev.interfaces.repositories.settings.SettingsRepository

/**
 * Use case para actualizar la configuración de apariencia del usuario
 *
 * @param repository Repositorio de configuraciones
 */
class UpdateAppearanceSettings(
    private val repository: SettingsRepository = SettingsRepositoryImp()
) : UseCase<UserSettings, Pair<User, AppearanceSettingsData>>() {

    /**
     * Ejecuta la actualización de la configuración de apariencia
     *
     * @param params Par que contiene el usuario y los datos de apariencia a actualizar
     * @return Either con el resultado de la operación
     */
    override suspend fun run(params: Pair<User, AppearanceSettingsData>): Either<Exception, UserSettings> {
        val (user, appearanceData) = params

        // Validar datos de apariencia antes de enviar al repositorio
        val validationResult = validateAppearanceData(appearanceData)
        if (validationResult != null) {
            return Either.Failure(validationResult)
        }

        return repository.updateAppearanceSettings(user, appearanceData)
    }

    /**
     * Valida los datos de apariencia antes de procesar la actualización
     *
     * @param appearanceData Datos de apariencia a validar
     * @return Exception si hay errores de validación, null si todo es válido
     */
    private fun validateAppearanceData(appearanceData: AppearanceSettingsData): Exception? {
        // Validar tema
        if (!isValidTheme(appearanceData.theme)) {
            return IllegalArgumentException("El tema debe ser 'light', 'dark' o 'system'")
        }

        return null
    }

    /**
     * Valida que el tema sea uno de los permitidos
     */
    private fun isValidTheme(theme: String): Boolean {
        return theme in listOf("light", "dark", "system")
    }

    /**
     * Valida que el color sea un código hexadecimal válido
     */
    private fun isValidHexColor(color: String): Boolean {
        val hexColorRegex = Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
        return hexColorRegex.matches(color)
    }

    /**
     * Valida que el tamaño de fuente sea uno de los permitidos
     */
    private fun isValidFontSize(fontSize: String): Boolean {
        return fontSize in listOf("small", "medium", "large")
    }

    /**
     * Valida que el idioma sea uno de los soportados por la aplicación
     */
    private fun isValidLanguage(language: String): Boolean {
        val supportedLanguages = listOf(
            "es", "en", "fr", "de", "it", "pt", "ru", "ja", "ko", "zh"
        )
        return language in supportedLanguages
    }

    companion object {
        // Colores de acento predefinidos
        val PRESET_ACCENT_COLORS = listOf(
            "#2196F3", // Blue
            "#4CAF50", // Green
            "#FF9800", // Orange
            "#9C27B0", // Purple
            "#F44336", // Red
            "#607D8B", // Blue Grey
            "#795548", // Brown
            "#009688", // Teal
            "#FF5722", // Deep Orange
            "#3F51B5"  // Indigo
        )

        // Temas disponibles
        val AVAILABLE_THEMES = listOf("light", "dark", "system")

        // Tamaños de fuente disponibles
        val FONT_SIZES = listOf("small", "medium", "large")

        // Idiomas soportados
        val SUPPORTED_LANGUAGES = mapOf(
            "es" to "Español",
            "en" to "English",
            "fr" to "Français",
            "de" to "Deutsch",
            "it" to "Italiano",
            "pt" to "Português",
            "ru" to "Русский",
            "ja" to "日本語",
            "ko" to "한국어",
            "zh" to "中文"
        )
    }
}
