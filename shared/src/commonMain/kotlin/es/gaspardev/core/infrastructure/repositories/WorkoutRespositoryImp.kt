package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.interfaces.repositories.WorkoutRespository

class WorkoutRespositoryImp : WorkoutRespository {
    override suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>> {
        return WorkoutRespository.API.getList(params = arrayOf(Pair("trainer_id", trainer.user.id.toString())))
    }

    override suspend fun getWorkoutsPlans(trainer: Trainer): Either<Exception, List<WorkoutPlan>> {
        return WorkoutRespository.API.getGenericList<WorkoutPlan>(
            listOf("plans"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun getWorkoutsPlanTemplates(trainer: Trainer): Either<Exception, List<WorkoutTemplate>> {
        return WorkoutRespository.API.getGenericList<WorkoutTemplate>(
            listOf("templates"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun createWorkout(workout: Workout, trainer: Trainer): Either<Exception, Boolean> {
        val result = WorkoutRespository.API.post(listOf("create"), Pair(workout, trainer))
        return if (result.isFailure) Either.Failure(result.getError()!!) else Either.Success(true)
    }
}