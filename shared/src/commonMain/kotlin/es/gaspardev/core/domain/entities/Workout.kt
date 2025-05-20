package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class Workout(
    val name: String,
    val description: String?,
    val notes: List<Note>,
    val exercises: Map<WeekDay, List<WorkoutExercise>>,
    val duration: Duration? = Duration.parse("W01"),
    val initalDate: Instant
) {
    companion object {
        const val URLPATH = "/workouts"
    }
} 