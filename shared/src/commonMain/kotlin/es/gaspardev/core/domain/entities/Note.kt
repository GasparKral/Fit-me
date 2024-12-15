package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val user: User,
    var message: String
) {

}