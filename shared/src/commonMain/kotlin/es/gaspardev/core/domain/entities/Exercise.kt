package es.gaspardev.core.domain.entities

import es.gaspardev.enums.BodyPart
import es.gaspardev.enums.grip.Grip
import kotlinx.serialization.Serializable

@Serializable
data class Exercise(
    val name: String,
    val bodyPart: BodyPart,
    var reps: Int,
    var sets: Int,
    val gripType: Grip?,
    var description: String,
    val author: Trainer?,
    var video: Resource?,
    val notes: List<Note>,
    val optionalExercises: List<Exercise>?
) {

    companion object {
        fun isValid(exercise: Exercise): Boolean {
            return exercise.name.isNotBlank() && exercise.description.length >= 10
        }
    }

}