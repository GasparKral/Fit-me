package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.core.domain.requests.CreateWorkoutRequest
import es.gaspardev.database.daos.WorkoutDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.logging.Level
import java.util.logging.Logger

fun Application.workout() {
    routing {
        get(Workout.URLPATH) {
            val trainerId = call.request.queryParameters["trainer_id"]
            if (trainerId != null) {
                val workout = suspendTransaction {
                    WorkoutDao().getWorkouts(trainerId)
                }

                call.respond(workout)
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

        post(Workout.URLPATH) {
            val body = call.receive<CreateWorkoutRequest>()
            val workout = body.workout
            val result = WorkoutDao().createWorkout(
                name = workout.name,
                description = workout.description,
                difficulty = workout.difficulty,
                duration = workout.duration,
                workoutType = workout.workoutType,
                createdBy = body.trainer.user.id
            )
            call.respond(result.getId())
        }

        patch(Workout.URLPATH) {
            val body = call.receive<WorkoutPlan>()
            WorkoutDao().updateWorkout(body)
        }

        delete(Workout.URLPATH) {
            val workoutId = call.request.queryParameters["workout_id"]
            if (workoutId != null) {
                try {
                    WorkoutDao().deleteWorkout(workoutId)
                    call.respond(HttpStatusCode.OK)
                } catch (e: NoSuchElementException) {
                    call.respondText("No se encontró el entrenamiento", status = HttpStatusCode.NotFound)
                } catch (e: Exception) {
                    call.respondText("Error al eliminar: ${e.message!!}", status = HttpStatusCode.ExpectationFailed)
                }
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }
    }
}