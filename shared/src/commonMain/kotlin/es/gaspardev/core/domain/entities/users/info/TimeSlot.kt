package es.gaspardev.core.domain.entities.users.info

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val start: Instant,
    val end: Instant
)
