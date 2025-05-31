package es.gaspardev.core.domain.entities.workouts

import es.gaspardev.core.domain.entities.users.Athlete
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CompletionWorkoutStatistic(
    val workout: Workout,
    val completeAt: Instant,
    val assignedAthlete: Athlete
)