package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.stadistics.ComprehensiveStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.interfaces.repositories.StadisticRepository
import kotlin.time.Duration

class StadisticRepositoryImp : StadisticRepository {
    override suspend fun getAthleteStadistics(
        athlete: Athlete,
        timeRange: Duration?
    ): Either<Exception, ComprehensiveStatistics> {
        return StadisticRepository.API.getSingleValue(
            listOf(athlete.user.id.toString()),
            params = timeRange?.let { arrayOf(Pair("timeRange", timeRange.toString())) }
                ?: emptyArray<Pair<String, String>>()
        )
    }

}