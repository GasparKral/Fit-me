package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class DietAPI(override val apiPath: String = SERVER_ADRESS + Diet.URLPATH) : API<Diet>() {
    override suspend fun post(segments: List<String>, body: Diet): Either<Exception, Diet> {
        TODO("Not yet implemented")
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Diet> {
        TODO("Not yet implemented")
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Diet>> {
        TODO("Not yet implemented")
    }

    suspend inline fun <reified T : Any> getGenericList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<T>> {

        return try {
            val response = `access$httpClient`.get(`access$apiPath`) {
                url {
                    pathSegments = Diet.URLPATH.split("/").filter { it.isNotEmpty() } + fragment
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

    override suspend fun patch(segments: List<String>, body: Diet): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    @PublishedApi
    internal val `access$httpClient`: HttpClient
        get() = httpClient

    @PublishedApi
    internal val `access$apiPath`: String
        get() = apiPath


}