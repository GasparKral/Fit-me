package es.gaspardev.core.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class Suplemment(
    val name: String,
    var ammount: Double,
    val notes: List<Note>
) {

}