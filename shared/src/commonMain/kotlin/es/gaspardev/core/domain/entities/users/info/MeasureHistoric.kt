package es.gaspardev.core.domain.entities.users.info

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class MeasureHistoric(
    val measurements: Measurements,
    val measureAt: Instant,
    val athlete: Int // ID ref
)