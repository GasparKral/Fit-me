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
    var name: String = "",
    var description: String = "",
    var dietType: DietType = DietType.ALL,
    var duration: Duration = 0.toDuration(DurationUnit.HOURS),
    val startAt: Instant = Clock.System.now(),
    val dishes: Map<WeekDay, List<DietDish>> = mapOf(),
    val notes: List<Note> = listOf()
) {

    companion object {
        const val URLPATH = "/diets"
    }

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

}