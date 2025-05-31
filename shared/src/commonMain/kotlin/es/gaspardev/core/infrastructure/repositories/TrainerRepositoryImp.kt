package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.LoginCredentials
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.interfaces.repositories.TrainerRepository

class TrainerRepositoryImp : TrainerRepository {
    private suspend fun getIntValueForTrainer(fragment: List<String>, trainer: Trainer): Int {
        var result = 0
        TrainerRepository.API.getSingleValue<Int>(
            fragment,
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        ).fold(
            { value -> result = value },
            { _ -> }
        )
        return result
    }

    override suspend fun getPendingWorkouts(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "pending"), trainer)
    }

    override suspend fun getActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "plans"), trainer)
    }

    override suspend fun getUpCommingSessions(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "session"), trainer)
    }

    override suspend fun getUnreadMessages(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "messages"), trainer)
    }

    override suspend fun getNewAthlete(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "new_athletes"), trainer)
    }

    override suspend fun getNewActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "new_active_plans"), trainer)
    }

    override suspend fun getNewMessages(trainer: Trainer): Int {
        return getIntValueForTrainer(listOf("data", "new_messages"), trainer)
    }

    override suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo {
        var returnValue = DashboardChartInfo()

        TrainerRepository.API.getSingleValue<DashboardChartInfo>(
            listOf("data", "dashboardChartInfo"),
            params = arrayOf(
                Pair("trainer_id", trainer.user.id.toString())
            )
        ).fold(
            { value -> returnValue = value },
            { _ -> }
        )
        return returnValue
    }

    override suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String> {
        return TrainerRepository.API.getSingleValue<String>(
            listOf("key_gen"),
            params = arrayOf(Pair("trainer_id", trainer.user.id.toString()))
        )
    }

    override suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun logIn(expectedUser: LoginCredentials): Either<Exception, Pair<Trainer, List<Athlete>>> {
        var result: Pair<Trainer, List<Athlete>>? = null

        TrainerRepository.API.getSingleValue<Pair<Trainer, List<Athlete>>>(
            listOf(),
            params = arrayOf(
                Pair("userIdentification", expectedUser.userIdetification),
                Pair("userPassword", expectedUser.userPassword)
            )
        ).fold(
            { value -> result = value },
            { err -> return Either.Failure(err) }
        )
        return Either.Success(result!!)
    }
}