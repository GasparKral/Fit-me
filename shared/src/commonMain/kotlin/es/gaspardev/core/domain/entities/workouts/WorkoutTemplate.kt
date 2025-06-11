package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutTemplate(
    var templateId: Int?,
    var name: String,
    var description: String,
    var difficulty: Difficulty = Difficulty.EASY,
    var workoutType: WorkoutType,
    val exercises: MutableMap<WeekDay, MutableList<WorkoutExercise>>
) {

    companion object {
        fun fromWorkout(workout: Workout): WorkoutTemplate {
            val template = WorkoutTemplate(
                templateId = workout.id,
                name = workout.name,
                description = workout.description,
                difficulty = workout.difficulty,
                workoutType = workout.workoutType,
                exercises = workout.exercises
            )
            return template
        }
    }
}