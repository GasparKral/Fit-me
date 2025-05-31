package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.users.User
import es.gaspardev.database.RegistKeyTable
import es.gaspardev.database.Trainers
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
                val keyValue = encrypt(trainerID + Clock.System.now().toString())
                suspendTransaction {
                    RegistKeyTable.insert {
                        it[key] = keyValue
                        it[trainer] =
                            Trainers.select(Trainers.id).where(Trainers.id eq trainerID.toInt())
                    }
                }
                call.respond(keyValue)
            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }
    }
}
