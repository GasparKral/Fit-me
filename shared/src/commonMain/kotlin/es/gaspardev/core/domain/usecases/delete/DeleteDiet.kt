package es.gaspardev.core.domain.usecases.delete

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.DietRepositoryImp
import es.gaspardev.interfaces.repositories.DietRepository

class DeleteDiet(
    private val repo: DietRepository = DietRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    override suspend fun run(params: Int): Either<Exception, None> {
        val result = repo.deleteDiet(params)
        return if (result?.isFailure == true) Either.Failure(result.error) else Either.Success(None)
    }
}