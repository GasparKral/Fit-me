package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp

class CreateNewTrainerUseCase(
    private val trainnerRepository: TrainerRepositoryImp
) : UseCase<Trainer, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, Trainer> {
        return trainnerRepository.save(params)
    }

}