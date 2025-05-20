package es.gaspardev.core.domain.usecases.update

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.TrainerPatchDTO
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.interfaces.repositories.TrainerRepository

class UpdateTrainerInfo(
    private val trainerRepository: TrainerRepository
) : UseCase<Trainer, TrainerPatchDTO>() {
    override suspend fun run(params: TrainerPatchDTO): Either<Exception, Trainer> {
        return trainerRepository.updateTrainerInfo(params)
    }

}