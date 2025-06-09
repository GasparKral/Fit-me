package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class WorkoutPlan(
    val workoutId: Int,
    var name: String,
    var description: String,
    var type: WorkoutType,
    var duration: Duration,
    val frequency: String,
    var difficulty: Difficulty,
    val asignedCount: Int,
    var exercises: MutableMap<WeekDay, MutableList<WorkoutExecise>>
) {
    companion object {
        fun fromWorkout(workout: Workout): WorkoutPlan {
            return WorkoutPlan(
                workoutId = workout.getId(),
                name = workout.name,
                description = workout.description,
                type = workout.workoutType,
                duration = workout.duration,
                frequency = workout.exercises.keys.size.toString(),
                difficulty = workout.difficulty,
                asignedCount = 0,
                exercises = workout.exercises
            )
        }
    }
}