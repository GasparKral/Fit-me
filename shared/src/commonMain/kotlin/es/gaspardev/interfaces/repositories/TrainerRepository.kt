package es.gaspardev.interfaces.repositories

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.dtos.RegisterTrainerData
import es.gaspardev.core.domain.dtos.TrainerPatchDTO
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.infrastructure.apis.TrainerAPI

interface TrainerRepository : UserRepository<Trainer> {

    companion object {
        val API = TrainerAPI()
    }

    suspend fun registerTrainer(newSportsmanData: RegisterTrainerData): Either<Exception, Trainer>
    suspend fun getPendingWorkouts(trainer: Trainer): Int
    suspend fun updateTrainerInfo(info: TrainerPatchDTO): Either<Exception, Trainer>
    suspend fun deleteAccount(trainer: Trainer): Either.Failure<Exception>?
    suspend fun getActivePlans(trainer: Trainer): Int
    suspend fun getUpCommingSessions(trainer: Trainer): Int
    suspend fun getUnreadMessages(trainer: Trainer): Int
    suspend fun getNewSposrtsman(trainer: Trainer): Int
    suspend fun getNewActivePlans(trainer: Trainer): Int
    suspend fun getNewMessages(trainer: Trainer): Int
    suspend fun getDashboardChartData(trainer: Trainer): DashboardChartInfo
    suspend fun generateRegistrationKey(trainer: Trainer): Either<Exception, String>
}