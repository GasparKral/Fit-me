package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.utils.SERVER_HTTPS_DIR
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class TrainerAPI(override val apiPath: String = SERVER_HTTPS_DIR + Trainer.URLPATH) : API<Trainer>() {
    override suspend fun post(route: String, body: Trainer): Either<Exception, Trainer> {
        TODO("Not yet implemented")
    }

    override suspend fun get(route: String, vararg params: String?): Either<Exception, Trainer> {
        val queryString = params.filterNotNull().joinToString("&") { it }
        val url: String = if (queryString.isNotEmpty()) {
            if (route.contains("?")) "$route&$queryString" else "$route?$queryString"
        } else {
            route
        }
        return try {
            val response = httpClient.get(url)

            when (response.status) {
                HttpStatusCode.OK -> {
                    return Either.Success(response.body())
                }

                HttpStatusCode.RequestTimeout -> {
                    Either.Failure(Exception("Fallo en la conexión"))
                }

                else -> {
                    Either.Failure(Exception("Unexpected status code: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

    suspend inline fun <reified T : Any> getSingleValue(route: String, vararg params: String?): Either<Exception, T> {
        // Filter non-null parameters and construct the query string
        val queryString = params.filterNotNull()
            .joinToString("&") { it }

        // Concatenate the route with the query string
        val url: String = if (queryString.isNotEmpty()) {
            if (route.contains("?")) "$route&$queryString" else "$route?$queryString"
        } else {
            route
        }

        return try {
            val response = `access$httpClient`.get(url)

            when (response.status) {
                HttpStatusCode.OK -> {
                    return Either.Success(response.body())
                }

                HttpStatusCode.RequestTimeout -> {
                    Either.Failure(Exception("Fallo en la conexión"))
                }

                else -> {
                    Either.Failure(Exception("Unexpected status code: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

    override suspend fun getList(route: String, vararg params: String?): Either<Exception, List<Trainer>> {
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

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

}