package es.gaspardev.core.domain.entities.users.info

import io.ktor.websocket.*

data class UserSession(
    val userId: Int,
    val session: WebSocketSession,
    val conversationId: Int
)