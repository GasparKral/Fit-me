package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.infrastructure.apis.TrainerAPI

interface TrainerRepository : UserRepository<Trainer, Athlete> {

    companion object {
        val API = TrainerAPI()
    }

    suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer>
    suspend fun getPendingWorkouts(trainer: Trainer): Int
    suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>?
    suspend fun getActivePlans(trainer: Trainer): Int
    suspend fun getUpCommingSessions(trainer: Trainer): Int
    suspend fun getUnreadMessages(trainer: Trainer): Int
    suspend fun getNewAthlete(trainer: Trainer): Int
    suspend fun getNewActivePlans(trainer: Trainer): Int
    suspend fun getNewMessages(trainer: Trainer): Int
    suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo
    suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String>
}