package es.gaspardev.core.domain.entities.users.info

import kotlinx.serialization.Serializable

@Serializable
data class Allergy(
    val id: Int,
    val name: String
)