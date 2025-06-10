package es.gaspardev.core.domain.entities.workouts

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
    private val id: Int? = null,
    var name: String = "",
    var description: String = "",
    var difficulty: Difficulty = Difficulty.EASY,
    var duration: Duration = 0.toDuration(DurationUnit.HOURS),
    val startAt: Instant = Clock.System.now(),
    var workoutType: WorkoutType = WorkoutType.ALL,
    var exercises: MutableMap<WeekDay, MutableList<WorkoutExecise>> = mutableMapOf()
) {

    companion object {
        const val URLPATH = "/workouts"

        fun fromPlan(plan: WorkoutPlan): Workout {
            return Workout(
                id = plan.workoutId,
                name = plan.name,
                description = plan.description,
                difficulty = plan.difficulty,
                duration = plan.duration,
                startAt = Instant.DISTANT_FUTURE,
                workoutType = plan.type,
                exercises = plan.exercises
            )
        }
    }

    fun getId(): Int? = id

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

    override fun toString(): String {
        val totalExercises = exercises.values.flatten().size
        val exercisesByDay = exercises.mapValues { it.value.size }
        val progression = "%.1f".format(getWorkoutProgression())

        return buildString {
            appendLine("Workout: $name")
            appendLine("Description: $description")
            appendLine("Type: $workoutType")
            appendLine("Difficulty: $difficulty")
            appendLine("Duration: ${duration.toIsoString()}")
            appendLine("Progress: $progression%")
            appendLine("Total exercises: $totalExercises")
            if (exercisesByDay.isNotEmpty()) {
                appendLine("Exercises by day:")
                exercisesByDay.forEach { (day, count) ->
                    appendLine("  $day: $count exercises")
                }
            }

        }.trimEnd()
    }
}