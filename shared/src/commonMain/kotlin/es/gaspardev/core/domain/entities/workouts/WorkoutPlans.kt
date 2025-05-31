package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.serialization.Serializable

@Serializable
data class WorkoutPlan(
    var name: String,
    var description: String,
    var type: WorkoutType,
    var duration: String,
    val frequency: String,
    var difficulty: Difficulty,
    val assignedCount: Int,
    var exercises: Map<WeekDay, List<WorkoutExecise>>
)