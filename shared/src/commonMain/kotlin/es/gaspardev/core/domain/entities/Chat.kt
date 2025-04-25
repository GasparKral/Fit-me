package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val trainer: Trainer,
    val sportman: Sportsman,
    val message: List<Message>
) {

}