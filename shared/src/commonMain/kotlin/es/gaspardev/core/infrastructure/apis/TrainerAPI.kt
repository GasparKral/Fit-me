package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import io.ktor.client.request.*
import io.ktor.http.*

class TrainerAPI(override val apiPath: String = Trainer.URLPATH) : API<Trainer>() {
    override suspend fun post(route: String, body: Trainer): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun get(route: String, vararg params: String?): Either<Exception, List<Trainer>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(route: String, vararg params: String?): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun patch(route: String, body: Any): Either.Failure<Exception>? {
        return try {
            val response = httpClient.patch("$apiPath/$route") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            null // PATCH exitoso
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

}