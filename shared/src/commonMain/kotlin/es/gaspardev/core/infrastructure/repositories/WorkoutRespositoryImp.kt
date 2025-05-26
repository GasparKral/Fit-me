package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.entities.Workout
import es.gaspardev.interfaces.repositories.WorkoutRespository

class WorkoutRespositoryImp : WorkoutRespository {
    override suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>> {
        return WorkoutRespository.API.getList(Workout.URLPATH, params = arrayOf("trainer_id=" + trainer.user.id))
    }
}