package es.gaspardev.core.domain.usecases.delete

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class DeleteWorkout(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<UseCase.None, Int>() {
    override suspend fun run(params: Int): Either<Exception, None> {
        val result = repo.deleteWorkout(params)
        return if (result?.isFailure == true) Either.Failure(result.error) else Either.Success(None)
    }
}