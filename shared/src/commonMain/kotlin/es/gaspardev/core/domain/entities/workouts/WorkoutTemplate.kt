package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutTemplate(
    var name: String,
    var description: String,
    var difficulty: Difficulty = Difficulty.EASY,
    var workoutType: WorkoutType,
    val exercises: MutableMap<WeekDay, MutableList<WorkoutExecise>>
)