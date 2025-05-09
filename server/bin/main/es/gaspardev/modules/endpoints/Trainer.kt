package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.Trainer
import es.gaspardev.db.SportsmanTable
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

fun Application.trainer() {

    routing {
        get(Trainer.URLPATH + "/pending") {
            val trainerID = call.request.queryParameters["trainer_id"]

            if (trainerID != null) {
                val value =
                    SportsmanTable.select(SportsmanTable.id).where(SportsmanTable.trainerId eq trainerID.toInt())
                        .count()
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }
    }

}
