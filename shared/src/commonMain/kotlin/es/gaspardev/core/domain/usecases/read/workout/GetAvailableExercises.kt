package es.gaspardev.core.domain.usecases.read.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.Exercise
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class GetAvailableExercises(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<List<Exercise>, UseCase.None>() {
    override suspend fun run(params: None): Either<Exception, List<Exercise>> {
        return repo.getAvailableExercises()
    }
}