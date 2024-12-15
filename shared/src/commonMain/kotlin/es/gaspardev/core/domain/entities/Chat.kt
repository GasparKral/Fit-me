package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val trainner: Trainner,
    val sportman: Sportman,
    val message: List<Message>
) {

}