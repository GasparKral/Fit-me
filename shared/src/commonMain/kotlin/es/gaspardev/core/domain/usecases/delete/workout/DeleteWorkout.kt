package es.gaspardev.core.domain.usecases.delete.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class DeleteWorkout(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    override suspend fun run(params: Int): Either<Exception, None> {
        return repo.deleteWorkout(params).foldValue(
            { _ -> Either.Success(None) },
            { err -> Either.Failure(err) }
        )
    }
}