package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.SportsmantRepositoryImp

class CreateNewSportsmanUseCase(
    private val sportmantRepository: SportsmantRepositoryImp
) : UseCase<Sportsman, Sportsman>() {
    override suspend fun run(params: Sportsman): Either<Exception, Sportsman> {
        return sportmantRepository.save(params)
    }

}