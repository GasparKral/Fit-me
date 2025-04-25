package es.gaspardev

import es.gaspardev.modules.endpoints.*
import es.gaspardev.modules.shockets.socket
import es.gaspardev.utils.SERVER_PORT
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(
        Netty, port = SERVER_PORT, module = Application::module, host = "localhost",
    ).start(wait = true)
}

fun Application.module() {
    /* CONFIGURACIÓN GENERAL */
    install(ContentNegotiation) {
        json(Json { prettyPrint = true })
    }
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Delete)
        anyHost()
    }
    install(Compression) {
        gzip()
    }

    /* Rutas de testing */
    routing {
        route("/ping"){
            handle {
                call.respond(HttpStatusCode.OK, "pong")
            }
        }
    }

    /* CARGA DE MÓDULOS */
    configureDatabases()
    socket()

    /* Modulos Referentes a recursos */
    upload()
    resources()

    user()
    trainer()
    sportsman()

}
