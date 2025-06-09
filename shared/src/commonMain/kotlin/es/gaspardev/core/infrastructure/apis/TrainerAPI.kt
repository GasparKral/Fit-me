package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class TrainerAPI(
    override val apiPath: String = SERVER_ADRESS + Trainer.URLPATH,
    override val urlPath: String = Trainer.URLPATH
) : API<Trainer>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, Trainer> {
        return performRequest<Trainer>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Trainer> {
        return performRequest<Trainer>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Trainer>> {
        return performRequest<List<Trainer>>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun delete(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, Unit> {
        return performDelete(segments, *params)
    }

    override suspend fun patch(segments: List<String>, body: Any): Either<Exception, Unit> {
        return performPatch(segments, body)
    }
}