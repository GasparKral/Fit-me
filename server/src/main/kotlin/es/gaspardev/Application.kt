package es.gaspardev

import es.gaspardev.utils.Logger
import io.ktor.server.application.*
import io.netty.handler.codec.http2.StreamBufferingEncoder

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

    /* embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
         .start(wait = true)*/
}

fun Application.module() {

}