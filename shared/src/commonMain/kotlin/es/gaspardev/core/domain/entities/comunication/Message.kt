package es.gaspardev.core.domain.entities.comunication

import es.gaspardev.enums.MessageStatus
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val userName: String,
    val sendAt: Instant,
    val content: String,
    val messageStatus: MessageStatus
)