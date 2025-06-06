package es.gaspardev.core.domain.requests

import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import kotlinx.serialization.Serializable

/**
 * Request para crear un nuevo workout
 */
@Serializable
data class CreateWorkoutRequest(
    val workout: Workout,
    val trainer: Trainer
)