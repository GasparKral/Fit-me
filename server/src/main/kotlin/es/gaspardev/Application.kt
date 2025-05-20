package es.gaspardev

import es.gaspardev.modules.endpoints.*
import es.gaspardev.modules.shockets.socket
import es.gaspardev.utils.*
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
import java.io.File

fun main() {

    startDB()

    embeddedServer(
        Netty, port = SERVER_PORT, module = Application::module, host = "localhost",
    ).start(wait = true)
}

fun Application.module() {
    /* CONFIGURACIÓN GENERAL */
    install(ContentNegotiation) {
        json(Json { prettyPrint = true;allowStructuredMapKeys = true })
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
        route("/ping") {
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

fun startDB() {
    try {
        // Verificar si el contenedor ya está corriendo
        val checkRunningProcessBuilder = ProcessBuilder("docker", "ps", "-q", "-f", "name=$DATA_BASE_NAME")
        val checkRunningProcess = checkRunningProcessBuilder.start()
        val runningOutput = checkRunningProcess.inputStream.bufferedReader().readText()

        if (runningOutput.isNotEmpty()) {
            println("El contenedor PostgreSQL ya está corriendo.")
            return
        }

        // Si no está corriendo, verificar si existe pero está detenido
        val checkExistsProcessBuilder = ProcessBuilder("docker", "ps", "-aq", "-f", "name=$DATA_BASE_NAME")
        val checkExistsProcess = checkExistsProcessBuilder.start()
        val existsOutput = checkExistsProcess.inputStream.bufferedReader().readText()

        if (existsOutput.isNotEmpty()) {
            // El contenedor existe pero está detenido, iniciarlo
            val startProcessBuilder = ProcessBuilder("docker", "start", DATA_BASE_NAME)
            val startProcess = startProcessBuilder.start()
            val startOutput = startProcess.inputStream.bufferedReader().readText()

            if (startProcess.waitFor() == 0) {
                println("Contenedor PostgreSQL existente iniciado: $startOutput")
                return
            } else {
                println("Error al iniciar el contenedor existente")
            }
        }

        // Si el contenedor no existe, crear uno nuevo
        val initScriptsPath = File("server/src/main/resources/DB").absolutePath

        val processBuilder = ProcessBuilder(
            "docker", "run",
            "--name", DATA_BASE_NAME,
            "-e", "POSTGRES_USER=${DATA_BASE_USERNAME}",
            "-e", "POSTGRES_PASSWORD=${DATA_BASE_PASSWORD}",
            "-e", "POSTGRES_DB=${DATA_BASE_NAME}",
            "-v", "$initScriptsPath:/docker-entrypoint-initdb.d",
            "-p", "${DATA_BASE_PORT}:5432",
            "-d",
            "postgres:15"
        )

        processBuilder.redirectErrorStream(true)

        val process = processBuilder.start()
        val output = process.inputStream.bufferedReader().readText()

        if (process.waitFor() == 0) {
            println("Nuevo contenedor PostgreSQL creado e iniciado: $output")
        } else {
            println("Error al crear el contenedor")
        }
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}