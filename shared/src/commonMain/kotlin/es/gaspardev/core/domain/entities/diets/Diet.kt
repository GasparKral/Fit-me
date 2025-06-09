package es.gaspardev.core.domain.entities.diets

import es.gaspardev.core.domain.entities.Note
import es.gaspardev.enums.DietType
import es.gaspardev.enums.WeekDay
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Serializable
data class Diet(
    private val id: Int? = null,
    var name: String = "",
    var description: String = "",
    var dietType: DietType = DietType.ALL,
    var duration: Duration = 0.toDuration(DurationUnit.HOURS),
    val startAt: Instant = Clock.System.now(),
    val dishes: MutableMap<WeekDay, MutableList<DietDish>> = mutableMapOf()
) {

    companion object {
        const val URLPATH = "/diets"

        fun fromPlan(plan: DietPlan): Diet {
            return Diet(
                id = plan.dietId,
                name = plan.name,
                description = plan.description,
                dietType = plan.type,
                duration = plan.duration,
                startAt = Instant.DISTANT_FUTURE,
                dishes = plan.dishes
            )
        }
    }

    fun getId(): Int = id ?: throw IllegalStateException("ID no puede ser null")

    fun getDietProgression(): Double {
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
        val totalDishes = dishes.values.flatten().size
        val dishesByDay = dishes.mapValues { it.value.size }
        val progression = "%.1f".format(getDietProgression())
        val durationInDays = duration.inWholeDays

        return buildString {
            appendLine("Diet Plan: $name")
            appendLine("Description: $description")
            appendLine("Type: $dietType")
            appendLine("Duration: $durationInDays days")
            appendLine("Progress: $progression%")
            appendLine("Total dishes: $totalDishes")
            if (dishesByDay.isNotEmpty()) {
                appendLine("Dishes by day:")
                dishesByDay.forEach { (day, count) ->
                    appendLine("  $day: $count dish(es)")
                }
            }
        }.trimEnd()
    }
}