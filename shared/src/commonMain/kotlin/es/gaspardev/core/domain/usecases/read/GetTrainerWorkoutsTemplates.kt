package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.WorkoutRespositoryImp
import es.gaspardev.interfaces.repositories.WorkoutRespository

class GetTrainerWorkoutsTemplates(
    private val repo: WorkoutRespository = WorkoutRespositoryImp()
) : UseCase<List<WorkoutTemplate>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, List<WorkoutTemplate>> {
        return repo.getWorkoutsPlanTemplates(params)
    }
}