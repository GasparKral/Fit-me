package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class GetTrainerDietsPlans(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<List<DietPlan>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<DietPlan>> {
        return repo.getDietsPlans(params)
    }
}