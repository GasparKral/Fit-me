package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.requests.CreateWorkoutRequest
import es.gaspardev.interfaces.repositories.WorkoutRepository

class WorkoutRepositoryImp : WorkoutRepository {
    override suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>> {
        return WorkoutRepository.API.getList(params = arrayOf(Pair("trainer_id", trainer.user.id.toString())))
    }

    override suspend fun getWorkoutsPlans(trainer: Trainer): Either<Exception, List<WorkoutPlan>> {
        return WorkoutRepository.API.getGenericList<WorkoutPlan>(
            listOf("plans"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun getWorkoutsPlanTemplates(trainer: Trainer): Either<Exception, List<WorkoutTemplate>> {
        return WorkoutRepository.API.getGenericList<WorkoutTemplate>(
            listOf("templates"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun createWorkout(workout: Workout, trainer: Trainer): Either<Exception, Int> {
        return WorkoutRepository.API.postGeneric<CreateWorkoutRequest, Int>(
            emptyList(),
            CreateWorkoutRequest(workout, trainer)
        )
            .foldValue<Either<Exception, Int>>(
                { value -> return Either.Success(value) },
                { err -> return Either.Failure(err) }
            )
    }

    override suspend fun deleteWorkout(workoutId: Int): Either.Failure<Exception>? {
        return WorkoutRepository.API.delete(
            emptyList(),
            params = arrayOf(Pair("workout_id", workoutId.toString()))
        )
    }

    override suspend fun updateWorkout(workout: WorkoutPlan): Either.Failure<Exception>? {
        return WorkoutRepository.API.patch(emptyList(), workout)
    }
}