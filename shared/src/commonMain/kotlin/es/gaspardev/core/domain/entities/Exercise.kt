package es.gaspardev.core.domain.entities

import es.gaspardev.enums.BodyPart
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val bodyPart: BodyPart,
    var description: String,
    val author: Trainner,
    var video: Resource?,
    val notes: List<Note>,
    val optionalExercises: List<Exercise>?
) {

}