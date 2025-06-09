package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.database.daos.DietDao
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
    }
}