package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class AthleteAPI(
    override val apiPath: String = SERVER_ADRESS + Athlete.URLPATH,
    override val urlPath: String = Athlete.URLPATH
) : API<Athlete>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, Athlete> {
        return performRequest<Athlete>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Athlete> {
        return performRequest<Athlete>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Athlete>> {
        return performRequest<List<Athlete>>(HttpMethod.Get, segments, null, *params)
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