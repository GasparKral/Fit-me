package es.gaspardev.core.domain.dtos.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppearanceSettingsData(
    val theme: String, // "light", "dark", "system"
)
