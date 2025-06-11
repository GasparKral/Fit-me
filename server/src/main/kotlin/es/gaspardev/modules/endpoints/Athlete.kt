package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.database.daos.AthleteDao
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.athlete() {

    route(Athlete.URLPATH) {

        get {
            val athleteId = call.request.queryParameters["athlete_id"]?.toInt() ?: return@get call.respondText(
                "Parámetros requeridos faltantes",
                status = HttpStatusCode.BadRequest
            )

            val result = transaction {
                AthleteDao.findAthleteByUserId(athleteId)?.toModel()
            }

            if (result != null) {
                call.respond(result)
            } else {
                call.respondText("No se encontró el usuario ", status = HttpStatusCode.NotFound)
            }
        }

        route("/data") {

            get("/workouthistory/{athlete_id}") {
                val athleteId = call.parameters["athlete_id"]?.toInt() ?: return@get call.respondText(
                    "Parámetros requeridos faltantes",
                    status = HttpStatusCode.BadRequest
                )
                call.respond(AthleteDao.getWorkoutHistory(athleteId))
            }

            get("/diethitory/{athlete_id}") {
                val athleteId = call.parameters["athlete_id"]?.toInt() ?: return@get call.respondText(
                    "Parámetros requeridos faltantes",
                    status = HttpStatusCode.BadRequest
                )
                call.respond(AthleteDao.getDietHistory(athleteId))
            }


        }
    }
}
