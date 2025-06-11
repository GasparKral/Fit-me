package es.gaspardev.core.domain.usecases.create.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class CreateNewWorkout(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<Int, Pair<Workout, Trainer>>() {
    override suspend fun run(params: Pair<Workout, Trainer>): Either<Exception, Int> {
        return repo.createWorkout(params.first, params.second)
    }
}