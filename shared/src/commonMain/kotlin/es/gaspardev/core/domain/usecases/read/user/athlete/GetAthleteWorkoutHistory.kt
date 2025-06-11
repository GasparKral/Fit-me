package es.gaspardev.core.domain.usecases.read.user.athlete

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.AthletetRepositoryImp
import es.gaspardev.interfaces.repositories.AthleteRepository

class GetAthleteWorkoutHistory(
    private val repo: AthleteRepository = AthletetRepositoryImp()
) : UseCase<List<CompletionWorkoutStatistic>, Athlete>() {
    override suspend fun run(params: Athlete): Either<Exception, List<CompletionWorkoutStatistic>> {
        return repo.getWorkoutsHistory(params)
    }
}