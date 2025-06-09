package es.gaspardev.core.domain.dtos.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppearanceSettingsData(
    val theme: String, // "light", "dark", "system"
    val accentColor: String, // color hex
    val fontSize: String, // "small", "medium", "large"
    val compactMode: Boolean,
    val showSidebar: Boolean,
    val animationsEnabled: Boolean,
    val language: String // "es", "en", etc.
)
