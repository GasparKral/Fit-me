package es.gaspardev.core.domain.usecases.read.user.athlete

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp
import es.gaspardev.interfaces.repositories.AthleteRepository

class GetAthleteInfo(
    private val repo: AthleteRepository = AthletetRepositoryImp()
) : UseCase<Athlete, Int>() {
    override suspend fun run(params: Int): Either<Exception, Athlete> {
        return repo.getAthleteInfo(params)
    }
}