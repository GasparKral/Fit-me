package es.gaspardev.core.domain.entities

import es.gaspardev.core.domain.entities.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val id: Int,
    val user: User,
    val content: String,
    val createdAt: Instant
)