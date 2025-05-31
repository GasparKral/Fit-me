package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.TrainerDashBoardInfo
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.interfaces.repositories.TrainerRepository


class LoadDashboardInfo(private val repo: TrainerRepository = TrainerRepositoryImp()) :
    UseCase<TrainerDashBoardInfo, Trainer>() {

    override suspend fun run(params: Trainer): Either<Exception, TrainerDashBoardInfo> {
        val pendingWorkouts = repo.getPendingWorkouts(params)
        val newsSportsman = repo.getNewAthlete(params)
        val plans = repo.getActivePlans(params)
        val newsPlans = repo.getNewActivePlans(params)
        val upcommingSessions = repo.getUpCommingSessions(params)
        val unreadMessages = repo.getUnreadMessages(params)
        val newMessages = repo.getNewMessages(params)

        return Either.Success(
            TrainerDashBoardInfo(
                pendingWorkouts,
                newsSportsman,
                plans,
                newsPlans,
                upcommingSessions,
                unreadMessages,
                newMessages
            )
        )
    }

}