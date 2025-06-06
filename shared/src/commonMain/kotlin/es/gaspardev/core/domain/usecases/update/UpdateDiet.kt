package es.gaspardev.core.domain.usecases.update

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class UpdateDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<DietPlan, DietPlan>() {
    override suspend fun run(params: DietPlan): Either<Exception, DietPlan> {
        val result = repo.updateDiet(params)
        return if (result?.isFailure == true) Either.Failure(result.error) else Either.Success(params)
    }
}