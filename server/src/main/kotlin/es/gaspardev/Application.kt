package es.gaspardev

import es.gaspardev.modules.*
import es.gaspardev.utils.Logger
import es.gaspardev.utils.SERVER_PORT
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import kotlinx.serialization.json.Json

fun main() {
    val runDBProcess = ProcessBuilder(
        "docker",
        "compose",
        "-f",
        "server/src/main/kotlin/es/gaspardev/db/docker-compose.yml", // Ruta relativa ajustada
        "up",
        "-d"
    ).inheritIO().start()

    val errorPrinter = Thread {
        runDBProcess.errorStream.bufferedReader().use { errorReader ->
            var line: String?
            while (errorReader.readLine().also { line = it } != null) {
                Logger.error(line!!)
            }
        }
    }

    errorPrinter.start()

    

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

    /* CARGA DE MÓDULOS */
    trainer()
    sportsman()
}
