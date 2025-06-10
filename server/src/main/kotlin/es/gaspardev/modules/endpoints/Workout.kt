package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.workouts.Workout
import es.gaspardev.core.domain.entities.workouts.WorkoutTemplate
import es.gaspardev.database.daos.WorkoutDao
import es.gaspardev.database.entities.ExerciseEntity
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.workout() {

    route(Workout.URLPATH) {

        get("/exercises") {
            val exercises = transaction {
                ExerciseEntity.all().map { it.toModel() }
            }

            call.respond(exercises)
        }

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
            call.respond(result.getId()!!)
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

        // Endpoints para templates
        post("/templates/{trainer_id}") {
            val trainerId = call.parameters["trainer_id"]?.toInt()
                ?: return@post call.respondText("Parámetro trainer_id requerido", status = HttpStatusCode.BadRequest)

            try {
                val template = call.receive<WorkoutTemplate>()

                // Validación básica
                if (template.name.isBlank()) {
                    return@post call.respondText(
                        "El nombre del template es requerido",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val createdTemplate = WorkoutDao.createWorkoutTemplate(
                    name = template.name,
                    description = template.description,
                    difficulty = template.difficulty,
                    workoutType = template.workoutType,
                    createdBy = trainerId
                )

                call.respond(HttpStatusCode.Created, createdTemplate)
            } catch (e: Exception) {
                call.respondText(
                    "Error al crear el template de entrenamiento: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        delete("/templates") {
            val templateId = call.request.queryParameters["template_id"]?.toIntOrNull()

            if (templateId == null) {
                call.respondText(
                    "Parámetro template_id requerido",
                    status = HttpStatusCode.BadRequest
                )
                return@delete
            }

            try {
                val deleted = WorkoutDao.deleteWorkoutTemplate(templateId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, "Template de entrenamiento eliminado correctamente")
                } else {
                    call.respondText(
                        "Template de entrenamiento no encontrado",
                        status = HttpStatusCode.NotFound
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al eliminar el template de entrenamiento: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("/template/{template_id}") {
            val templateId = call.parameters["template_id"]?.toIntOrNull()
                ?: return@get call.respondText(
                    "Parámetro template_id requerido",
                    status = HttpStatusCode.BadRequest
                )

            try {
                val template = WorkoutDao.findWorkoutTemplateById(templateId)
                if (template != null) {
                    call.respond(HttpStatusCode.OK, template.toModel())
                } else {
                    call.respondText(
                        "Template de entrenamiento no encontrado",
                        status = HttpStatusCode.NotFound
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al obtener el template de entrenamiento: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        post("/templates/create/{trainer_id}") {
            val trainerID = call.parameters["template_id"]?.toIntOrNull()
                ?: return@post call.respondText(
                    "Parámetro template_id requerido",
                    status = HttpStatusCode.BadRequest
                )

            try {
                val template = call.receive<WorkoutTemplate>()

                val result = transaction {
                    val newTemplate = WorkoutDao.createWorkoutTemplate(
                        name = template.name,
                        description = template.description,
                        difficulty = template.difficulty,
                        workoutType = template.workoutType,
                        createdBy = trainerID
                    )

                    template.exercises.forEach { (day, exercises) ->
                        exercises.forEach { exercise ->
                            WorkoutDao.addWorkoutTemplateExercises(
                                templateId = newTemplate.getId(),
                                exerciseId = exercise.exercise.id,
                                weekDay = day,
                                reps = exercise.reps,
                                sets = exercise.sets,
                                isOptional = exercise.isOptional()
                            )
                        }
                    }

                    newTemplate
                }
                call.respond(result)
            } catch (e: Exception) {
                call.respondText(
                    "Error al obtener el template de entrenamiento: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}