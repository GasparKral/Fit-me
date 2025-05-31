package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.enums.Difficulty
import es.gaspardev.enums.WeekDay
import es.gaspardev.enums.WorkoutType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
data class Workout(
    var name: String = "",
    var description: String = "",
    var difficulty: Difficulty = Difficulty.EASY,
    var duration: Duration = 0.toDuration(DurationUnit.HOURS),
    val startAt: Instant = Clock.System.now(),
    var workoutType: WorkoutType = WorkoutType.ALL,
    var exercises: Map<WeekDay, List<WorkoutExecise>> = mapOf(),
    val notes: List<Note> = listOf()
) {

    companion object {
        const val URLPATH = "/workouts"
    }

    fun getWorkoutProgression(): Double {
        val now = Clock.System.now()
        val endTime = startAt + duration

        // If workout hasn't started yet
        if (now < startAt) return 0.0

        // If workout is finished
        if (now >= endTime) return 1.0

        // Calculate progression percentage (0.0 to 1.0)
        val totalDuration = endTime - startAt
        val elapsed = now - startAt
        return (elapsed.inWholeMilliseconds.toDouble() / totalDuration.inWholeMilliseconds.toDouble()) * 100
    }
}