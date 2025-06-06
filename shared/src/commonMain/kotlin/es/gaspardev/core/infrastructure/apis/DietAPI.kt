package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class DietAPI(
    override val apiPath: String = SERVER_ADRESS + Diet.URLPATH,
    override val urlPath: String = Diet.URLPATH
) : API<Diet>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, Diet> {
        return performRequest<Diet>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Diet> {
        return performRequest<Diet>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Diet>> {
        return performRequest<List<Diet>>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either.Failure<Exception>? {
        return performDelete(segments, *params)
    }

    override suspend fun patch(segments: List<String>, body: Any): Either.Failure<Exception>? {
        return performPatch(segments, body)
    }
}