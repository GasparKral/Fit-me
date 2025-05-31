package es.gaspardev.modules.endpoints

import io.ktor.server.application.*
import io.ktor.server.routing.*

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