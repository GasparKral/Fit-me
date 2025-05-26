package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.Workout
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRespositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRespository

class GetTrainerWorkouts(
    private val repo: WorkoutRespository = WorkoutRespositoryImp()
) : UseCase<List<Workout>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<Workout>> {
        return repo.getWorkouts(params)
    }

}
