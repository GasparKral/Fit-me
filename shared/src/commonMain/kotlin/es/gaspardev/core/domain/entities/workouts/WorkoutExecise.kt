package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Note
import kotlinx.serialization.Serializable
import java.lang.IllegalArgumentException

@Serializable
data class WorkoutExecise(
    var reps: Int,
    var sets: Int,
    private val isOption: Boolean = false,
    val exercise: Exercise,
    val notes: List<Note>
) {
    var optionalExercise: MutableList<Exercise> = mutableListOf()
        private set

    fun addOptionalExercise(execise: Exercise): Either.Failure<Exception>? {
        if (!this.isOption) {
            optionalExercise.add(exercise)
            return null
        } else {
            return Either.Failure(IllegalArgumentException("Los ejercicios opcionales no pueden tener opciones"))
        }
    }

}
