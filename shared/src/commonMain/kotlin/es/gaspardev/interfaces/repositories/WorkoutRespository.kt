package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.Workout
import es.gaspardev.core.infrastructure.apis.WorkoutAPI

interface WorkoutRespository {

    companion object {
        val API = WorkoutAPI()
    }

    suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>>

}