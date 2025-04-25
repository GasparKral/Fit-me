package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val start: LocalTime,
    val end: LocalTime
)

@Serializable
data class DayAvailability(
    val weekDay: WeekDay,
    val timeSlots: List<TimeSlot> = emptyList()
) {
    val available: Boolean
        get() = timeSlots.isNotEmpty()
}

@Serializable
data class WeeklyAvailability(
    val availability: Map<WeekDay, DayAvailability>
)
