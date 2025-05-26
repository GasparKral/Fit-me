package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CompletedDiet(
    val diet: Diet,
    val completeAt: Instant,
    val notes: List<Note>
) {

}