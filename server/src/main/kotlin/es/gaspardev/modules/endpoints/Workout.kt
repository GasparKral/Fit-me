package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutPlan
import es.gaspardev.database.daos.WorkoutDao
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.workout() {

    route(Workout.URLPATH) {

        get("/{trainer_id}") {
            val trainerId = call.parameters["trainer_id"]?.toInt()
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            call.respond(WorkoutDao.getWorkouts(trainerId))
        }

        get("/plans/{trainer_id}") {
            val trainerId = call.parameters["trainer_id"]?.toInt()
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            call.respond(WorkoutDao.getPlans(trainerId))
        }

        get("/templates/{trainer_id}") {
            val trainerId = call.parameters["trainer_id"]?.toInt()
                ?: return@get call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            call.respond(WorkoutDao.getTemplates(trainerId))
        }

        post("/{trainer_id}") {
            val trainerId = call.parameters["trainer_id"]?.toInt()
                ?: return@post call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            val workout = call.receive<Workout>()

            val result = WorkoutDao.createWorkout(
                name = workout.name,
                description = workout.description,
                difficulty = workout.difficulty,
                duration = workout.duration,
                workoutType = workout.workoutType,
                createdBy = trainerId
            )
            call.respond(result.getId())
        }


        delete {
            val workoutId = call.request.queryParameters["workout_id"]
            if (workoutId != null) {
                try {
                    WorkoutDao.deleteWorkout(workoutId)
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