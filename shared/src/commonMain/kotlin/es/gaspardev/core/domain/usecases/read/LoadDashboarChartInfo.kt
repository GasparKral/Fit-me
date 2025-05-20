package es.gaspardev.core.domain.usecases.read

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.core.domain.usecases.UseCase
import es.gaspardev.core.infrastructure.repositories.TrainerRepositoryImp
import es.gaspardev.interfaces.repositories.TrainerRepository
import kotlinx.datetime.Instant

class LoadDashboarChartInfo(
    val repo: TrainerRepository = TrainerRepositoryImp()
) : UseCase<Array<List<Pair<Instant, Long>>>, Trainer>() {
    override suspend fun run(params: Trainer): Either<Exception, Array<List<Pair<Instant, Long>>>> {
        return Either.Success(repo.getDashboardChartData(params))
    }

}