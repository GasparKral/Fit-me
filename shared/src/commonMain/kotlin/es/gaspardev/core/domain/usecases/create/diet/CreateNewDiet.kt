package es.gaspardev.core.domain.usecases.create.diet

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class CreateNewDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<Int, Pair<Diet, Trainer>>() {
    override suspend fun run(params: Pair<Diet, Trainer>): Either<Exception, Int> {
        return repo.createDiet(params.first, params.second)
    }
}