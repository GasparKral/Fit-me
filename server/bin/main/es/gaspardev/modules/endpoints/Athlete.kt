package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.users.Athlete
import es.gaspardev.database.daos.AthleteDao
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.athlete() {

    routing {
        get(Athlete.URLPATH + "/data/workouthistory") {
            val athleteId = call.request.queryParameters["user_id"]

            if (athleteId != null) {
                val history = AthleteDao().getWorkoutHistory(athleteId.toInt()).map { it.toModel() }
                call.respond(history)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Athlete.URLPATH + "/data/diethitory") {
            val athleteId = call.request.queryParameters["user_id"]

            if (athleteId != null) {
                val history = AthleteDao().getDietHistory(athleteId.toInt()).map { it.toModel() }
                call.respond(history)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        get(Athlete.URLPATH + "/data/sessions") {
            val athleteId = call.request.queryParameters["user_id"]

            if (athleteId != null) {
                val sessions = AthleteDao().getCommingSessions(athleteId.toInt()).map { it.toModel() }
                call.respond(sessions)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }
    }

}
