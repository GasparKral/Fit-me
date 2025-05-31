package es.gaspardev.core.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentials(
    val userIdetification: String,
    val userPassword: String
)