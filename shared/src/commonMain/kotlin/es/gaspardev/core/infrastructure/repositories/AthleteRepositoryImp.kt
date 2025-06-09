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
            listOf("data", "workouthistory", athlete.user.id.toString())
        )
    }

    override suspend fun getCommingSessions(athlete: Athlete): Either<Exception, List<Session>> {
        return AthleteRepository.API.getGenericList<Session>(
            listOf("data", "sessions", athlete.user.id.toString())
        )
    }

    override suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Triple<Athlete, List<Trainer>, List<Conversation>>> {
        return AthleteRepository.API.getSingleValue<Triple<Athlete, List<Trainer>, List<Conversation>>>(
            segments = listOf(expectedUser.userIdetification, expectedUser.userPassword)
        ).foldValue(
            { value -> Either.Success(value) },
            { err -> Either.Failure(err) }
        )
    }

    override suspend fun getConversations(user: User): Either<Exception, List<Conversation>> {
        TODO("Not yet implemented")
    }


}