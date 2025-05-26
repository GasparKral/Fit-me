package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.dtos.RegisterSportsmanData
import es.gaspardev.core.domain.entities.Sportsman
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.sportsman() {

    routing {
        post(Sportsman.URLPATH + "/create") {
            val newSportsman = call.receive<RegisterSportsmanData>()

            suspendTransaction {


            }
        }
    }

}
