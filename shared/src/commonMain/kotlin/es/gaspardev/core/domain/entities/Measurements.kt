package es.gaspardev.core.domain.entities

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class Measurements(
    var weight: Double? = 0.toDouble(),
    var height: Double? = 0.toDouble(),
    var bodyFat: Int? = 0,
    var lastUpdated: Instant? = Clock.System.now()
) {

}