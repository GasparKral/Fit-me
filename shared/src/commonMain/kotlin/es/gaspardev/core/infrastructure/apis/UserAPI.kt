package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.User
import es.gaspardev.utils.SERVER_HTTPS_DIR
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class UserAPI(override val apiPath: String = SERVER_HTTPS_DIR + User.URLPATH) : API<User>() {
    override suspend fun post(route: String, body: User): Either<Exception, User> {

        return try {
            val response = httpClient.post(route) {
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

    // TODO: CAMBIAR A USER EN VEZ DE LIST<USER>
    override suspend fun get(route: String, vararg params: String?): Either<Exception, User> {
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

    override suspend fun getList(route: String, vararg params: String?): Either<Exception, List<User>> {
        TODO("Not yet implemented")
    }


    override suspend fun delete(route: String, vararg params: String?): Either.Failure<Exception>? {
        // Filter non-null parameters and construct the query string
        val queryString = params.filterNotNull()
            .joinToString("&") { "param=$it" }

        // Concatenate the route with the query string
        val url: String = if (queryString.isNotEmpty()) {
            if (route.contains("?")) "$route&$queryString" else "$route?$queryString"
        } else {
            route
        }

        return try {
            val response = httpClient.delete(url)

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

    override suspend fun patch(route: String, body: Any): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }


}