package es.gaspardev.core.domain.usecases.read.user.trainer

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRepositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRepository

class GetTrainerWorkoutsPlans(
    private val repo: WorkoutRepository = WorkoutRepositoryImp()
) : UseCase<List<WorkoutPlan>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<WorkoutPlan>> {
        return repo.getWorkoutsPlans(params)
    }
}
