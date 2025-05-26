package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.Workout
import es.gaspardev.db.mappings.workouts.WorkoutDao
import es.gaspardev.db.mappings.workouts.WorkoutEntity
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.workout() {
    routing {
        get(Workout.URLPATH) {
            val trainerId = call.request.queryParameters["trainer_id"]
            if (trainerId != null) {
                val workout = suspendTransaction {
                    WorkoutEntity.all().filter {

                    }.map { WorkoutDao().toDomain(it) }
                }
            }
        }
    }
}