package es.gaspardev.core.domain.entities.comunication

import es.gaspardev.core.domain.entities.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Int,
    val trainer: User,
    val athlete: User,
    val messages: List<Message> = emptyList(),
    val lastMessage: Message? = null,
    val lastActivity: Instant? = null,
    val unreadCount: Int = 0,
    val isOnline: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant? = null
)