package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Exercise
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.infrastructure.apis.WorkoutAPI

interface WorkoutRepository {

    companion object {
        val API = WorkoutAPI()
    }

    suspend fun getWorkouts(trainer: Trainer): Either<Exception, List<Workout>>
    suspend fun getWorkoutsPlans(trainer: Trainer): Either<Exception, List<WorkoutPlan>>
    suspend fun getWorkoutsPlanTemplates(trainer: Trainer): Either<Exception, List<WorkoutTemplate>>
    suspend fun createWorkout(workout: Workout, trainer: Trainer): Either<Exception, Int>
    suspend fun deleteWorkout(workoutId: Int): Either<Exception, Unit>
    suspend fun updateWorkout(workout: WorkoutPlan): Either<Exception, WorkoutPlan>
    suspend fun createWorkoutTemplate(template: WorkoutTemplate, trainer: Trainer): Either<Exception, Int>
    suspend fun deleteWorkoutTemplate(templateId: Int): Either<Exception, Unit>
    suspend fun getAvailableExercises(): Either<Exception, List<Exercise>>
    suspend fun assignWorkouteToAthlete(workoutId: Int, id: Int): Either<Exception, Unit>
}