package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.stadistics.ComprehensiveStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.StadisticRepositoryImp
import es.gaspardev.interfaces.repositories.StadisticRepository
import kotlin.time.Duration

class GetAthleteStadistics(
    private val repo: StadisticRepository = StadisticRepositoryImp()
) : UseCase<ComprehensiveStatistics, Pair<Athlete, Duration?>>() {
    override suspend fun run(params: Pair<Athlete, Duration?>): Either<Exception, ComprehensiveStatistics> {
        return repo.getAthleteStadistics(params.first, params.second)
    }
}