package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutTemplate(
    private val templateId: Int? = null,
    var name: String,
    var description: String,
    var difficulty: Difficulty = Difficulty.EASY,
    var workoutType: WorkoutType,
    val exercises: MutableMap<WeekDay, MutableList<WorkoutExecise>>
) {

    companion object {
        fun fromWorkout(workout: Workout): WorkoutTemplate {
            return WorkoutTemplate(
                templateId = workout.getId(),
                name = workout.name,
                description = workout.description,
                difficulty = workout.difficulty,
                workoutType = workout.workoutType,
                exercises = workout.exercises
            )
        }
    }

    fun getId(): Int = templateId ?: throw IllegalStateException("Template ID no puede ser null")
}