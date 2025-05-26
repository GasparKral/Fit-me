package es.gaspardev.core.domain.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class CompletedWorkout(
    val workout: Workout,
    val completedAt: Instant,
    val notes: List<Note>
)
