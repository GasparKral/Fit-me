package es.gaspardev.core.domain.usecases.delete.diet

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class DeleteDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    override suspend fun run(params: Int): Either<Exception, None> {
        return repo.deleteDiet(params).foldValue(
            { _ -> Either.Success(None) },
            { err -> Either.Failure(err) }
        )
    }
}