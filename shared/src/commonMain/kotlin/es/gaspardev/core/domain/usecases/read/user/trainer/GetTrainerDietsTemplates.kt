package es.gaspardev.core.domain.usecases.read.user.trainer

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.DietTemplate
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class GetTrainerDietsTemplates(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<List<DietTemplate>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<DietTemplate>> {
        return repo.getDietsTemplates(params)
    }
}