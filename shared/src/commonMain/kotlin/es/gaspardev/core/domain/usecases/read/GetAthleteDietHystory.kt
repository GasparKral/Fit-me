package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class GetAthleteDietHystory(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<List<CompletionDietStatistics>, Athlete>() {
    override suspend fun run(params: Athlete): Either<Exception, List<CompletionDietStatistics>> {
        return repo.getDietsHistory(params)
    }
}