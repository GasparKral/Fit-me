package es.gaspardev.core.domain.entities.users.info

import io.ktor.websocket.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val session: WebSocketSession,
    val conversationId: Int
)