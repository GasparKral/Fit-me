package es.gaspardev.core.domain.entities

import es.gaspardev.enums.MessageType
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val user: User,
    val messageType: MessageType,
    var message: String,
    var media: Resource?,
    val postTime: Instant,
) {

}