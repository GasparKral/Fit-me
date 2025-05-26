package es.gaspardev.core.domain.dtos

import es.gaspardev.core.domain.entities.CompletedDiet
import es.gaspardev.core.domain.entities.CompletedWorkout
import kotlinx.serialization.Serializable

@Serializable
data class DashboardChartInfo(
    val workouts: List<CompletedWorkout> = listOf(),
    val diets: List<CompletedDiet> = listOf()
)