package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.comunication.Session
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp
import es.gaspardev.interfaces.repositories.AthleteRepository

class GetAthleteCommingSessions(
    private val repo: AthleteRepository = AthletetRepositoryImp()
) : UseCase<List<Session>, Athlete>() {
    override suspend fun run(params: Athlete): Either<Exception, List<Session>> {
        return repo.getCommingSessions(params)
    }
}