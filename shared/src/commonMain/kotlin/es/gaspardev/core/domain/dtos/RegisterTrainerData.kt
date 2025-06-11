package es.gaspardev.core.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class RegisterTrainerData(
    val userName: String,
    val password: String,
    val email: String,
    val specialization: String?,
    val yearsOfExperience: Int
) 