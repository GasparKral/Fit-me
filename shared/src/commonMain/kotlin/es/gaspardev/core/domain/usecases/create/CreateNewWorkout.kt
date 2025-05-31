package es.gaspardev.core.domain.usecases.create

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRespositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRespository

class CreateNewWorkout(
    private val repo: WorkoutRespository = WorkoutRespositoryImp()
) : UseCase<Boolean, Pair<Workout, Trainer>>() {
    override suspend fun run(params: Pair<Workout, Trainer>): Either<Exception, Boolean> {
        return repo.createWorkout(params.first, params.second)
    }
}