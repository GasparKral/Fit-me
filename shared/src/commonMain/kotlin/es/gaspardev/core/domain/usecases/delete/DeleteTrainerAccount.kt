package es.gaspardev.core.domain.usecases.delete

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.interfaces.repositories.TrainerRepository

class DeleteTrainerAccount(
    private val trainerRepository: TrainerRepository = TrainerRepositoryImp()
) : UseCase<UseCase.None, Trainer>() {

    override suspend fun run(params: Trainer): Either<Exception, None> {
        val request = trainerRepository.deleteAccount(params)

        return when (request) {
            is Either.Failure -> Either.Failure(request.error)
            else -> Either.Success(None)
        }
    }

}