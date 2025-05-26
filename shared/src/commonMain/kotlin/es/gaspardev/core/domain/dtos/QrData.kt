package es.gaspardev.core.domain.dtos

import kotlinx.serialization.Serializable

@Serializable
data class QrData(
    val token: String,
    val action: String = "register"
)