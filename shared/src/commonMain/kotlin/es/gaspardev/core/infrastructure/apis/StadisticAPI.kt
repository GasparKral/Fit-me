package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.utils.SERVER_ADRESS

class StadisticAPI(
    override val apiPath: String = "$SERVER_ADRESS/statistics",
    override val urlPath: String = "/statistics"
) : API<Unit>() {
    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Unit>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun patch(segments: List<String>, body: Any): Either<Exception, Unit> {
        TODO("Not yet implemented")
    }
}