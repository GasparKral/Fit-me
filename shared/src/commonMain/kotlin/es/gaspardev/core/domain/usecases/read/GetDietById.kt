package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class GetDietById(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<DietPlan, Int>() {
    override suspend fun run(params: Int): Either<Exception, DietPlan> {
        return repo.getDietById(params)
    }
}
