package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Sportsman

class SportsmanAPI(override val apiPath: String = Sportsman.URLPATH) : API<Sportsman>() {
    override suspend fun post(route: String, body: Sportsman, exceptionMessage: String?): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun get(route: String): Either<Exception, Sportsman> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(route: String): Either<Exception, List<Sportsman>> {
        TODO("Not yet implemented")
    }

}