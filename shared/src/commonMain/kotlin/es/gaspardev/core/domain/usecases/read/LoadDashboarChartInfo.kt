package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.interfaces.repositories.TrainerRepository

class LoadDashboarChartInfo(
    private val repo: TrainerRepository = TrainerRepositoryImp()
) : UseCase<DashboardChartInfo, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, DashboardChartInfo> {
        return Either.Success(repo.getDashboardChartData(params))
    }

}