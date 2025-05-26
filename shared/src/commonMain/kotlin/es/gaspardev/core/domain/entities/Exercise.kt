package es.gaspardev.core.domain.entities

import es.gaspardev.enums.BodyPart
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseBase(
    val id: Int,
    val name: String,
    val bodyPart: BodyPart,
    val description: String,
    val author: Trainer?,
    val video: Resource?
) {
    companion object {
        fun isValid(exercise: ExerciseBase): Boolean {
            return exercise.name.isNotBlank() && exercise.description.length >= 10
        }
    }
}

@Serializable
data class WorkoutExercise(
    val base: ExerciseBase,
    val reps: Int,
    val sets: Int,
    val note: List<Note> = listOf(),
    private var optionalExercises: MutableList<WorkoutExercise>? = null,
    val isOption: Boolean
) {
    fun addOptionalExercise(exercise: WorkoutExercise) {
        if (!isOption) {
            if (optionalExercises == null) {
                optionalExercises = mutableListOf(exercise)
            } else {
                optionalExercises!!.add(exercise)
            }
        }
    }

    fun getOptionalExercises(): MutableList<WorkoutExercise>? {
        return this.optionalExercises
    }
}