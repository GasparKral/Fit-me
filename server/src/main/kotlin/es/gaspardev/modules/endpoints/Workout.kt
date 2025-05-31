package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.database.daos.WorkoutDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.workout() {
    routing {
        get(Workout.URLPATH) {
            val trainerId = call.request.queryParameters["trainer_id"]
            if (trainerId != null) {
                val workout = suspendTransaction {
                    WorkoutDao().getWorkouts(trainerId)
                }
            }
        }

        get(Workout.URLPATH + "/plans") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val plans = suspendTransaction {
                    WorkoutDao().getPlans(trainerID)
                }

                call.respond(plans)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Workout.URLPATH + "/templates") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val plans = suspendTransaction {
                    WorkoutDao().getTemplates(trainerID)
                }

                call.respond(plans)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        post(Workout.URLPATH + "/create") {
            val body = call.receive<Pair<Workout, Trainer>>()
            val workout = body.first
            WorkoutDao().createWorkout(
                name = workout.name,
                description = workout.description,
                difficulty = workout.difficulty,
                duration = workout.duration,
                workoutType = workout.workoutType,
                createdBy = body.second.user.id
            )
        }
    }
}