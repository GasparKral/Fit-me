package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman
import es.gaspardev.utils.SERVER_HTTPS_DIR

class SportsmanAPI(override val apiPath: String = SERVER_HTTPS_DIR + Sportsman.URLPATH) : API<Sportsman>() {
    override suspend fun post(route: String, body: Sportsman): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun get(route: String, vararg params: String?): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(route: String, vararg params: String?): Either<Exception, List<Sportsman>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(route: String, vararg params: String?): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun patch(route: String, body: Any): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }


}