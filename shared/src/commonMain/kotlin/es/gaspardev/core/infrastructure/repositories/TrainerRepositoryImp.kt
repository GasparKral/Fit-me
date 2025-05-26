package es.gaspardev.core.infrastructure.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.LoginUserInfo
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.dtos.TrainerPatchDTO
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.interfaces.repositories.TrainerRepository

class TrainerRepositoryImp : TrainerRepository {
    private suspend fun getIntValueForTrainer(endpoint: String, trainer: Trainer): Int {
        var result = 0
        TrainerRepository.API.getSingleValue<Int>(
            endpoint,
            params = arrayOf("trainer_id=" + trainer.user.id)
        ).fold(
            { value -> result = value },
            { /* en caso de error, result sigue siendo 0 */ }
        )
        return result
    }

    override suspend fun getPendingWorkouts(trainer: Trainer): Int {
        return getIntValueForTrainer("/pending", trainer)
    }

    override suspend fun getActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/plans", trainer)
    }

    override suspend fun getUpCommingSessions(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/session", trainer)
    }

    override suspend fun getUnreadMessages(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/messages", trainer)
    }

    override suspend fun getNewSposrtsman(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/new_athletes", trainer)
    }

    override suspend fun getNewActivePlans(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/new_active_plans", trainer)
    }

    override suspend fun getNewMessages(trainer: Trainer): Int {
        return getIntValueForTrainer("/data/new_messages", trainer)
    }

    override suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo {
        var returnValue = DashboardChartInfo()

        TrainerRepository.API.getSingleValue<DashboardChartInfo>(
            "/data/dashboardChartInfo",
            params = arrayOf("trainer_id=" + trainer.user.id)
        ).fold(
            { value -> returnValue = value },
            { err -> throw err }
        )
        return returnValue
    }

    override suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String> {
        return TrainerRepository.API.getSingleValue<String>(
            "key_gen",
            params = arrayOf("trainer_id=" + trainer.user.id)
        )
    }

    override suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }


    override suspend fun updateTrainerInfo(info: TrainerPatchDTO): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun logIn(expectedUser: LoginUserInfo): Either<Exception, Trainer> {
        return TrainerRepository.API.get(
            params = arrayOf(
                "userIdentification=" + expectedUser.userIdetification,
                "userPassword=" + expectedUser.userPassword
            )
        )
    }


}
