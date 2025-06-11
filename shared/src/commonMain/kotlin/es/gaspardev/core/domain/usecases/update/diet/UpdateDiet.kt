package es.gaspardev.core.domain.usecases.update.diet

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class UpdateDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<DietPlan, DietPlan>() {
    override suspend fun run(params: DietPlan): Either<Exception, DietPlan> {
        return repo.updateDiet(params).foldValue(
            { value -> Either.Success(value) },
            { err -> Either.Failure(err) }
        )
    }
}