package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserAPI(override val apiPath: String = SERVER_ADRESS + User.URLPATH) : API<User>() {
    override suspend fun post(segments: List<String>, body: User): Either<Exception, User> {

        return try {
            val response = httpClient.post(apiPath) {
                url {
                    pathSegments = listOf("user") + segments
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val users: User = response.body()
                    Either.Success(users)
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

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, User> {
        return try {
            val response = httpClient.delete(apiPath) {
                url {
                    pathSegments = listOf("user") + segments
                    params.forEach { parameters.append(it.first, it.second) }
                }
            }

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

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }


    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either.Failure<Exception>? {
        return try {
            val response = httpClient.delete(apiPath) {
                url {
                    pathSegments = listOf("user") + segments
                    params.forEach { parameters.append(it.first, it.second) }
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    null
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

    override suspend fun patch(segments: List<String>, body: User): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }


}