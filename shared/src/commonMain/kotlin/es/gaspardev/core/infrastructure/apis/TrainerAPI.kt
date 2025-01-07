package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class TrainerAPI(override val apiPath: String = Trainer.URLPATH) : API<Trainer>() {

    override suspend fun post(route: String, body: Trainer, exceptionMessage: String?): Either<Exception, Trainer> {
        return try {
            val response = httpClient.post(apiPath + route) {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body<Trainer>()

            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(Exception(exceptionMessage ?: "An error occurred", e))
        }
    }

    override suspend fun get(route: String): Either<Exception, Trainer> {
        return try {
            val response = httpClient.get(apiPath + route) {
                contentType(ContentType.Application.Json)
            }.body<Trainer>()
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(Exception("Failed to fetch data from $route", e))
        }
    }

    override suspend fun getList(route: String): Either<Exception, List<Trainer>> {
        return try {
            val response = httpClient.get(apiPath + route) {
                contentType(ContentType.Application.Json)
            }.body<List<Trainer>>()
            Either.Success(response)
        } catch (e: Exception) {
            Either.Failure(Exception("Failed to fetch data from $route", e))
        }
    }
}