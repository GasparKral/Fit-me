package es.gaspardev.core.domain.usecases

import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.dtos.DashboardChartInfo

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Exception, Type>

    suspend operator fun invoke(params: Params, onSuccess: (Type) -> Unit, onFailure: (Exception) -> Unit) {
        run(params).fold(
            onSuccess,
            onFailure
        )
    }

    object None

}
