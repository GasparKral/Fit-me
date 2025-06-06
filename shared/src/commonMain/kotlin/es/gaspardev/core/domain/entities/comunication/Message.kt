package es.gaspardev.core.domain.entities.comunication

import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String, // UUID o ID único del mensaje
    val conversationId: Int,
    val senderId: Int, // ID del usuario que envía
    val senderName: String, // Nombre para mostrar
    val receiverId: Int, // ID del usuario que recibe
    val content: String,
    val messageType: MessageType = MessageType.TEXT,
    val sentAt: Instant,
    val deliveredAt: Instant? = null,
    val readAt: Instant? = null,
    val messageStatus: MessageStatus,
    val isFromCurrentUser: Boolean = false // Para UI
)