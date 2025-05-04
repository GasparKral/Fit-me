package es.gaspardev.modules.endpoints


import es.gaspardev.core.domain.entities.User

import es.gaspardev.db.UserDAO
import es.gaspardev.db.UserTable
import es.gaspardev.db.daoToModel
import es.gaspardev.utils.decode64
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or

fun Application.user() {
    routing {

        // Ruta para obtener los detalles del usuario
        get(User.URLPATH) {
            val userIdentification = call.request.queryParameters["userIdentification"]
            val userPassword = call.request.queryParameters["userPassword"]

            if (userIdentification != null && userPassword != null) {
                // Realiza la consulta a la base de datos

                val user =
                    suspendTransaction {
                        UserDAO.find {
                            ((UserTable.name eq userIdentification) or (UserTable.email eq userIdentification) and (UserTable.password eq userPassword))
                        }.map(::daoToModel).firstOrNull()
                    }

                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respondText("Usuario no encontrado", status = HttpStatusCode.NotFound)
                }

            } else {
                call.respondText("Parámetros requeridos faltantes", status = HttpStatusCode.BadRequest)
            }
        }

        post(User.URLPATH + "/bind") {

        }
    }
}
