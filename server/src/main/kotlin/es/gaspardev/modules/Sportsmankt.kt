package es.gaspardev.modules

import es.gaspardev.core.domain.entities.Sportsman
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.sportsman() {

    routing {
        route(Sportsman.URLPATH) {
            handle {
                call.respondText("Halo desde sportman")
            }
        }
    }

}
