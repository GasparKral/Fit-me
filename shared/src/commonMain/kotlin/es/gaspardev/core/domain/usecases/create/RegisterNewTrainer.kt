package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.DAOs.RegisterTrainerData
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.TrainerRepository

class RegisterNewTrainer(
    private val trainerRespository: TrainerRepository
) : UseCase<Trainer, RegisterTrainerData>() {
    override suspend fun run(params: RegisterTrainerData): Either<Exception, Trainer> {
        return trainerRespository.registerTrainer(params)
    }

}