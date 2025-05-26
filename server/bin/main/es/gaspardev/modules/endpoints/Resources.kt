package es.gaspardev.modules.endpoints

import es.gaspardev.core.domain.entities.Resource
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll

fun Application.resources() {
    routing {
        route("/resources") {
            get {
                val resources = suspendTransaction {

                }
            }
        }
    }
}