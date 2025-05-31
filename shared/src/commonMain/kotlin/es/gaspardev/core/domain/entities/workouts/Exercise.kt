package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.BodyPart
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val bodyPart: BodyPart
)