package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.stadistics.ComprehensiveStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.infrastructure.apis.StadisticAPI
import kotlin.time.Duration

interface StadisticRepository {

    companion object {
        val API = StadisticAPI()
    }

    suspend fun getAthleteStadistics(
        athlete: Athlete,
        timeRange: Duration? = null
    ): Either<Exception, ComprehensiveStatistics>

}