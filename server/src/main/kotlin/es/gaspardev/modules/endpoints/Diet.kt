package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.diets.DietPlan
import es.gaspardev.database.daos.DietDao
import es.gaspardev.database.entities.DietPlanEntity
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.diet() {

    route(Diet.URLPATH) {

        get("/plans/{trainer_id}") {
            val trainerID = call.parameters["trainer_id"]?.toIntOrNull() ?: return@get call.respondText(
                "Parámetros requeridos faltantes o inválidos",
                status = HttpStatusCode.BadRequest
            )
            call.respond(DietDao.getPlans(trainerID))
        }

        get("/templates/{trainer_id}") {
            val trainerID = call.parameters["trainer_id"]?.toIntOrNull() ?: return@get call.respondText(
                "Parámetros requeridos faltantes o inválidos",
                status = HttpStatusCode.BadRequest
            )
            call.respond(DietDao.getTemplates(trainerID))
        }

        post("/create/{trainer_id}") {
            val trainerID = call.parameters["trainer_id"]?.toIntOrNull() ?: return@post call.respondText(
                "Parámetros requeridos faltantes o inválidos",
                status = HttpStatusCode.BadRequest
            )
            try {
                val diet = call.receive<Diet>()

                // Validación básica
                if (diet.name.isBlank()) {
                    return@post call.respondText(
                        "El nombre de la dieta es requerido",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val createdDiet = DietDao.createDiet(
                    name = diet.name,
                    description = diet.description,
                    dietType = diet.dietType,
                    duration = diet.duration,
                    createdBy = trainerID
                )

                call.respond(HttpStatusCode.Created, createdDiet)
            } catch (e: Exception) {
                call.respondText(
                    "Error al crear la dieta",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        patch {
            try {
                val dietPlan = call.receive<DietPlan>()

                // Validación básica
                if (dietPlan.name.isBlank()) {
                    return@patch call.respondText(
                        "El nombre de la dieta es requerido",
                        status = HttpStatusCode.BadRequest
                    )
                }

                val updatedDiet = DietDao.updateDiet(dietPlan)

                if (updatedDiet != null) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respondText(
                        "Dieta no encontrada",
                        status = HttpStatusCode.NotFound
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al actualizar la dieta: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        delete {
            val dietId = call.request.queryParameters["diet_id"]?.toIntOrNull()

            if (dietId == null) {
                call.respondText(
                    "Parámetro diet_id requerido",
                    status = HttpStatusCode.BadRequest
                )
                return@delete
            }

            try {
                val deleted = DietDao.deleteDiet(dietId)

                if (deleted) {
                    call.respond(HttpStatusCode.OK, "Dieta eliminada correctamente")
                } else {
                    call.respondText(
                        "Dieta no encontrada",
                        status = HttpStatusCode.NotFound
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al eliminar la dieta: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        get("/plan/{diet_id}") {
            val dietId = call.parameters["diet_id"]?.toIntOrNull() ?: return@get call.respondText(
                "Parámetro diet_id requerido",
                status = HttpStatusCode.BadRequest
            )

            try {
                val dietEntity = DietDao.findDietById(dietId)
                if (dietEntity != null) {
                    val dietPlan = DietPlanEntity.findById(dietId)?.toModel()
                    if (dietPlan != null) {
                        call.respond(HttpStatusCode.OK, dietPlan)
                    } else {
                        call.respondText(
                            "Dieta no encontrada",
                            status = HttpStatusCode.NotFound
                        )
                    }
                } else {
                    call.respondText(
                        "Dieta no encontrada",
                        status = HttpStatusCode.NotFound
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al obtener la dieta: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }

        patch("/assign") {
            try {
                val assignData = call.receive<Map<String, Int>>()
                val dietId = assignData["dietId"] ?: return@patch call.respondText(
                    "dietId requerido",
                    status = HttpStatusCode.BadRequest
                )
                val athleteId = assignData["athleteId"] ?: return@patch call.respondText(
                    "athleteId requerido",
                    status = HttpStatusCode.BadRequest
                )

                val assigned = DietDao.assignDietToAthlete(dietId, athleteId)

                if (assigned) {
                    call.respond(HttpStatusCode.OK, "Dieta asignada correctamente")
                } else {
                    call.respondText(
                        "Error al asignar la dieta",
                        status = HttpStatusCode.BadRequest
                    )
                }
            } catch (e: Exception) {
                call.respondText(
                    "Error al asignar la dieta: ${e.message}",
                    status = HttpStatusCode.InternalServerError
                )
            }
        }
    }
}