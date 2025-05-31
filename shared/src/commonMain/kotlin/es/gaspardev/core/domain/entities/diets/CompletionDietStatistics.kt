package es.gaspardev.core.domain.entities.diets

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CompletionDietStatistics(
    val diet: Diet,
    val completeAt: Instant,
    val asignedAthlete: Int // ID ref
)
