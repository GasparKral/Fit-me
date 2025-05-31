package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.infrastructure.apis.WorkoutAPI

interface WorkoutRespository {

    companion object {
        val API = WorkoutAPI()
    }

    suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>>
    suspend fun getWorkoutsPlans(trainer: Trainer): Either<Exception, List<WorkoutPlan>>
    suspend fun getWorkoutsPlanTemplates(trainer: Trainer): Either<Exception, List<WorkoutTemplate>>
    suspend fun createWorkout(workout: Workout, trainer: Trainer): Either<Exception, Boolean>

}