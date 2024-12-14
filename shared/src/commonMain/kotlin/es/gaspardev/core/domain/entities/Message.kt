package es.gaspardev.core.domain.entities

import es.gaspardev.enums.MessageType
import kotlinx.datetime.Instant

data class Message(
    val id: Int,
    val user: User,
    val messageType: MessageType,
    var message: String,
    var media: Resource?,
    val postTime: Instant,
) {

}