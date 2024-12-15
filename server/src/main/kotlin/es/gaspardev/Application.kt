package es.gaspardev

import es.gaspardev.utils.Logger
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

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

    /* CONFIGURATION  */
    install(ContentNegotiation) {
        json()
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

    /* MODULE ROUTING */
    common()
    web()
    desktopAndMobile()
    desktop()
    mobile()
}

fun Application.common() {

}

fun Application.web() {

}

fun Application.desktopAndMobile() {

}

fun Application.desktop() {

}

fun Application.mobile() {

}

