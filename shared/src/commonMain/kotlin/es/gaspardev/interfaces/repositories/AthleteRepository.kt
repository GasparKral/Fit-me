package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.RegisterAthleteData
import es.gaspardev.core.domain.entities.diets.CompletionDietStatistics
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.core.infrastructure.apis.AthleteAPI

interface AthleteRepository : UserRepository<Athlete, Trainer> {

    companion object {
        val API = AthleteAPI()
    }

    suspend fun registerAthlete(newAthleteData: RegisterAthleteData): Either<Exception, Athlete>
    suspend fun getWorkoutsHistory(athlete: Athlete): Either<Exception, List<CompletionWorkoutStatistic>>

    suspend fun getDietsHistory(athlete: Athlete): Either<Exception, List<CompletionDietStatistics>>

    suspend fun getAthleteInfo(athleteId: Int): Either<Exception, Athlete>
}