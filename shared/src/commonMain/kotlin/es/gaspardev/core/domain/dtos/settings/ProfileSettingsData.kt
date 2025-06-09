package es.gaspardev.core.domain.dtos.settings

import kotlinx.serialization.Serializable

@Serializable
data class ProfileSettingsData(
    val fullName: String,
    val email: String,
    val phone: String?,
    val specialization: String?,
    val yearsOfExperience: Int?,
    val bio: String?,
    val profileImageUrl: String?
)
