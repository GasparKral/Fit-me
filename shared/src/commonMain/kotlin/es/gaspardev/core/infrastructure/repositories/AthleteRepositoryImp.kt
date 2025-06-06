package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.dtos.RegisterAthleteData
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Session
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.core.domain.entities.workouts.CompletionWorkoutStatistic
import es.gaspardev.interfaces.repositories.AthleteRepository

class AthletetRepositoryImp : AthleteRepository {
    override suspend fun registerAthlete(newAthleteData: RegisterAthleteData): Either<Exception, Athlete> {
        TODO("Not yet implemented")
    }

    override suspend fun getWorkoutsHistory(athlete: Athlete): Either<Exception, List<CompletionWorkoutStatistic>> {
        return AthleteRepository.API.getGenericList<CompletionWorkoutStatistic>(
            listOf("data", "workouthistory"),
            params = arrayOf(Pair("athlete_id", athlete.user.id.toString()))
        )
    }

    override suspend fun getCommingSessions(athlete: Athlete): Either<Exception, List<Session>> {
        return AthleteRepository.API.getGenericList<Session>(
            listOf("data", "sessions"),
            params = arrayOf(Pair("athlete_id", athlete.user.id.toString()))
        )
    }

    override suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Pair<Athlete, List<Trainer>>> {
        var athelte: Athlete? = null
        AthleteRepository.API.get(
            params = arrayOf(
                Pair("userIdentification", "expectedUser.userIdetification"),
                Pair("userPassword", expectedUser.userPassword)
            )
        ).fold(
            { value -> athelte = value },
            { err -> return Either.Failure(err) }
        )

        val trainer = Athlete

        val res: Pair<Athlete, List<Trainer>> = Pair(athelte!!, listOf())
        return Either.Success(res)
    }

    override suspend fun getComunications(user: User): Either<Exception, List<Conversation>> {
        TODO("Not yet implemented")
    }


}