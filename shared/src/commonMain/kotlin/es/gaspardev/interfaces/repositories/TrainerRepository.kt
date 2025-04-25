package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterTrainerData
import es.gaspardev.core.domain.DAOs.TrainerPatchDTO
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.infrastructure.apis.TrainerAPI

interface TrainerRepository {

    companion object {
        val API = TrainerAPI()
    }

    suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer>

    suspend fun getPendingWorkouts(trainer: User): Int
    suspend fun updateTrainerInfo(info: TrainerPatchDTO): Either<Exception, Trainer>

    suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>?

}