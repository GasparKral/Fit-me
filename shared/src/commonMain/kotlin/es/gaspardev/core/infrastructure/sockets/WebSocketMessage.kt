package es.gaspardev.core.infrastructure.shockets

import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
sealed class WebSocketMessage {
    @Serializable
    data class SendMessage(
        val content: String,
        val messageType: MessageType = MessageType.TEXT,
        val tempId: String? = null
    ) : WebSocketMessage()

    @Serializable
    data class MessageReceived(
        val message: Message
    ) : WebSocketMessage()

    @Serializable
    data class MessageStatusUpdate(
        val messageId: String,
        val status: MessageStatus,
        val timestamp: Instant
    ) : WebSocketMessage()

    @Serializable
    data class TypingIndicator(
        val userId: Int,
        val conversationId: Int,
        val isTyping: Boolean
    ) : WebSocketMessage()

    @Serializable
    data class UserOnlineStatus(
        val userId: Int,
        val isOnline: Boolean,
        val lastSeen: Instant? = null
    ) : WebSocketMessage()

    @Serializable
    data class Error(
        val code: String,
        val message: String,
        val tempId: String? = null
    ) : WebSocketMessage()

    @Serializable
    data object Ping : WebSocketMessage()

    @Serializable
    data object Pong : WebSocketMessage()
}

@Serializable
data class UserSession(
    val userId: Int,
    val conversationId: Int,
    val sessionId: String,
    val connectedAt: Instant
)
