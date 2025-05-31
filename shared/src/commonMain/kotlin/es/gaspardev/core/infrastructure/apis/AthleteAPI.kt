package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AthleteAPI(override val apiPath: String = SERVER_ADRESS + Athlete.URLPATH) : API<Athlete>() {
    override suspend fun post(segments: List<String>, body: Athlete): Either<Exception, Athlete> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Athlete> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Athlete>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun patch(segments: List<String>, body: Athlete): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    suspend inline fun <reified T : Any> getGenericList(
        fragment: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<T>> {
        return try {
            val response = `access$httpClient`.get(`access$apiPath`) {
                url {
                    pathSegments = Athlete.URLPATH.split("/").filter { it.isNotEmpty() } + fragment
                    params.forEach { parameters.append(it.first, it.second) }
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    Either.Success(response.body())
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

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

    @PublishedApi
    internal val `access$apiPath`: String
        get() = apiPath


}