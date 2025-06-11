package es.gaspardev.core.domain.usecases.update.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class UpdateWorkout(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<WorkoutPlan, WorkoutPlan>() {
    override suspend fun run(params: WorkoutPlan): Either<Exception, WorkoutPlan> {
        return repo.updateWorkout(params).foldValue(
            { value -> Either.Success(value) },
            { err -> Either.Failure(err) }
        )
    }
}