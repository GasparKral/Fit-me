package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRespositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRespository

class GetTrainerWorkoutsPlans(
    private val repo: WorkoutRespository = WorkoutRespositoryImp()
) : UseCase<List<WorkoutPlan>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<WorkoutPlan>> {
        return repo.getWorkoutsPlans(params)
    }
}
