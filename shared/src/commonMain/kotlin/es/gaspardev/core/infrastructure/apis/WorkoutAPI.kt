package es.gaspardev.core.infrastructure.apis

import API
import es.gaspardev.auxliars.Either
import es.gaspardev.core.domain.entities.Workout
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class WorkoutAPI(override val apiPath: String = Workout.URLPATH) : API<Workout>() {
    override suspend fun post(route: String, body: Workout): Either<Exception, Workout> {
        TODO("Not yet implemented")
    }

    override suspend fun get(route: String, vararg params: String?): Either<Exception, List<Workout>> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(route: String, vararg params: String?): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }

    override suspend fun patch(route: String, body: Any): Either.Failure<Exception>? {
        TODO("Not yet implemented")
    }


}