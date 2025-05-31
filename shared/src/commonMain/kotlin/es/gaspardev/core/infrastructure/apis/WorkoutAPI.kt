package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.util.concurrent.TimeoutException

class WorkoutAPI(override val apiPath: String = SERVER_ADRESS + Workout.URLPATH) : API<Workout>() {
    override suspend fun post(segments: List<String>, body: Workout): Either<Exception, Workout> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Workout> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Workout>> {
        return try {
            val response = httpClient.get(apiPath) {
                url {
                    pathSegments = Workout.URLPATH.split("/").filter { it.isNotEmpty() } + segments
                    params.forEach { parameters.append(it.first, it.second) }
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    return Either.Success(response.body())
                }

                HttpStatusCode.RequestTimeout -> {
                    Either.Failure(TimeoutException("Fallo en la conexión"))
                }

                else -> {
                    Either.Failure(Exception("Unexpected status code: ${response.status}"))
                }
            }
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }

    suspend inline fun <reified T : Any> getGenericList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<T>> {

        return try {
            val response = `access$httpClient`.get(`access$apiPath`) {
                url {
                    pathSegments
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

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun patch(segments: List<String>, body: Workout): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

    @PublishedApi
    internal val `access$apiPath`: String
        get() = apiPath


}