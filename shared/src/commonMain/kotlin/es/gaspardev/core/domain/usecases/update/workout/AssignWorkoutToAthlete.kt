package es.gaspardev.core.domain.usecases.update.workout

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class AssignWorkoutToAthlete(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<WorkoutPlan, Pair<WorkoutPlan, Athlete>>() {
    override suspend fun run(params: Pair<WorkoutPlan, Athlete>): Either<Exception, WorkoutPlan> {
        val (workoutPlan, athlete) = params
        return repo.assignWorkouteToAthlete(workoutPlan.workoutId!!, athlete.user.id).foldValue(
            { _ ->
                val updatedPlan = workoutPlan.copy(asignedCount = workoutPlan.asignedCount + 1)
                Either.Success(updatedPlan)
            },
            { err -> Either.Failure(err) }
        )
    }
}