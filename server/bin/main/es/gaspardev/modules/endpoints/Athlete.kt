package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.database.daos.AthleteDao
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.athlete() {

    route(Athlete.URLPATH) {

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

            get("/sessions/{athlete_id}") {
                val athleteId = call.parameters["athlete_id"]?.toInt() ?: return@get call.respondText(
                    "Parámetros requeridos faltantes",
                    status = HttpStatusCode.BadRequest
                )
                call.respond(AthleteDao.getCommingSessions(athleteId))
            }
        }
    }
}
