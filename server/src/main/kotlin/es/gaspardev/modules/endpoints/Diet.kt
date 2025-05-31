package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.diets.Diet
import es.gaspardev.core.domain.entities.users.Trainer
import es.gaspardev.database.daos.DietDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.diet() {
    routing {
        /*  get(Diet.URLPATH) {
              val trainerId = call.request.queryParameters["trainer_id"]
              if (trainerId != null) {
                  val workout = suspendTransaction {
                     // DietDao().get(trainerId)
                  }
              }
          }*/

        get(Diet.URLPATH + "/plans") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val plans = suspendTransaction {
                    DietDao().getPlans(trainerID)
                }

                call.respond(plans)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Diet.URLPATH + "/templates") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val plans = suspendTransaction {
                    DietDao().getTemplates(trainerID)
                }

                call.respond(plans)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        post(Diet.URLPATH + "/create") {
            val body = call.receive<Pair<Diet, Trainer>>()
            val diet = body.first

            DietDao().createDiet(
                name = diet.name,
                description = diet.description,
                dietType = diet.dietType,
                duration = diet.duration,
                createdBy = body.second.user.id
            )
        }
    }
}