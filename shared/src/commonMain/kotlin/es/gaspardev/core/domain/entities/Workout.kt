package es.gaspardev.core.domain.entities

import es.gaspardev.enums.WeekDay
import kotlinx.serialization.Serializable

@Serializable
data class Workout(
    val notes: List<Note>,
    val exercies: Map<WeekDay, List<Exercise>>
) {

}
