package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.User
import es.gaspardev.db.RegistKeyTable
import es.gaspardev.db.TrainersTable
import es.gaspardev.utils.encrypt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert

fun Application.user() {
    routing {
        get(User.URLPATH + "/key_gen") {
            val trainerID = call.request.queryParameters["trainer_id"]
            if (trainerID != null) {
                val key = encrypt(trainerID + Clock.System.now().toString())
                suspendTransaction {
                    RegistKeyTable.insert {
                        it[RegistKeyTable.key] = key
                        it[trainer] =
                            TrainersTable.select(TrainersTable.id).where(TrainersTable.userId eq trainerID.toInt())
                    }
                }
                call.respond(key)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }
    }
}
