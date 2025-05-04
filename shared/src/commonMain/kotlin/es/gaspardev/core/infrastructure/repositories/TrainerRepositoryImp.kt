package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterTrainerData
import es.gaspardev.core.domain.DAOs.TrainerPatchDTO
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.User
import es.gaspardev.core.infrastructure.apis.TrainerAPI
import es.gaspardev.interfaces.repositories.EntitieRepository
import es.gaspardev.interfaces.repositories.TrainerRepository
import es.gaspardev.interfaces.repositories.UserRepository

class TrainerRepositoryImp : TrainerRepository {
    override suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun getPendingWorkouts(trainer: User): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateTrainerInfo(info: TrainerPatchDTO): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }


}
