package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.utils.SERVER_ADRESS
import io.ktor.http.*

class WorkoutAPI(
    override val apiPath: String = SERVER_ADRESS + Workout.URLPATH,
    override val urlPath: String = Workout.URLPATH
) : API<Workout>() {

    override suspend fun <T : Any> post(segments: List<String>, body: T): Either<Exception, Workout> {
        return performRequest<Workout>(HttpMethod.Post, segments, body)
    }

    override suspend fun get(segments: List<String>, vararg params: Pair<String, String>): Either<Exception, Workout> {
        return performRequest<Workout>(HttpMethod.Get, segments, null, *params)
    }

    override suspend fun getList(
        segments: List<String>,
        vararg params: Pair<String, String>
    ): Either<Exception, List<Workout>> {
        return performRequest<List<Workout>>(HttpMethod.Get, segments, null, *params)
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