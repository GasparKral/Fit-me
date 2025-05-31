package es.gaspardev.core.domain.dtos


import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import kotlinx.serialization.Serializable

@Serializable
data class DashboardChartInfo(
    val workouts: List<CompletionWorkoutStatistic> = listOf(),
    val diets: List<CompletionDietStatistics> = listOf()
)