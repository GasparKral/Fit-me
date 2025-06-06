package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class UserAPI(
    override val apiPath: String = SERVER_ADRESS + User.URLPATH,
    override val urlPath: String = User.URLPATH
) : API<User>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, User> {
        return performRequest<User>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, User> {
        // Corregido: ahora usa GET en lugar de DELETE
        return performRequest<User>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<User>> {
        return performRequest<List<User>>(HttpMethod.Get, segments, null, *params)
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