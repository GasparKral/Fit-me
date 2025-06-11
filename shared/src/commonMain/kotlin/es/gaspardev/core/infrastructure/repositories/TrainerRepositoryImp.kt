package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.interfaces.repositories.TrainerRepository

class TrainerRepositoryImp : TrainerRepository {
    private suspend fun getIntValueForTrainer(end: String, trainer: Trainer): Int {
        return TrainerRepository.API.getSingleValue<Int>(
            listOf("data", trainer.user.id.toString(), end)
        ).foldValue(
            { value -> value },
            { _ -> 0 }
        )
    }

    override suspend fun getPendingWorkouts(trainer: Trainer): Int {
        return getIntValueForTrainer("pending", trainer)
    }

    override suspend fun getActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer("plans", trainer)
    }

    override suspend fun getUpCommingSessions(trainer: Trainer): Int {
        return getIntValueForTrainer("session", trainer)
    }

    override suspend fun getUnreadMessages(trainer: Trainer): Int {
        return getIntValueForTrainer("messages", trainer)
    }

    override suspend fun getNewAthlete(trainer: Trainer): Int {
        return getIntValueForTrainer("new_athletes", trainer)
    }

    override suspend fun getNewActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer("new_active_plans", trainer)
    }

    override suspend fun getNewMessages(trainer: Trainer): Int {
        return getIntValueForTrainer("new_messages", trainer)
    }

    override suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo {
        return TrainerRepository.API.getSingleValue<DashboardChartInfo>(
            listOf("data", trainer.user.id.toString(), "dashboardChartInfo")
        ).foldValue(
            { value -> value },
            { _ -> DashboardChartInfo() }
        )
    }

    override suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String> {
        return TrainerRepository.API.getSingleValue<String>(
            listOf("key_gen"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun registerTrainer(newAthleteData: RegisterTrainerData): Either<Exception, Trainer> {
        return TrainerRepository.API.post(
            segments = listOf("register"),
            body = newAthleteData
        )
    }

    override suspend fun deleteAccount(trainer: Trainer): Either<Exception, Unit> {
        return TrainerRepository.API.delete(
            listOf("/acount"),
            params = arrayOf(Pair("user_id", trainer.user.id.toString()))
        )
    }

    override suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Triple<Trainer, List<Athlete>, List<Conversation>>> {
        return TrainerRepository.API.getSingleValue<Triple<Trainer, List<Athlete>, List<Conversation>>>(
            segments = listOf(expectedUser.userIdetification, expectedUser.userPassword)
        ).foldValue(
            { value -> Either.Success(value) },
            { err -> Either.Failure(err) }
        )
    }

    override suspend fun getConversations(user: User): Either<Exception, List<Conversation>> {
        return TrainerRepository.API.getGenericList<Conversation>(
            listOf(user.id.toString(), "comunication")
        )
    }
}