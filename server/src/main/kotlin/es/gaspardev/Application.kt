package es.gaspardev

import es.gaspardev.modules.endpoints.*
import es.gaspardev.modules.shockets.chat
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
import java.sql.DriverManager
import kotlin.system.exitProcess

fun main() {
    // Iniciar y esperar a que PostgreSQL esté listo
    startDB()

    // Esperar a que la base de datos esté disponible
    waitForDatabase()

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
    chat()

    routing {
        trainer()
        athlete()
        workout()
        diet()
        comunication()
        statistics()
    }

}

fun startDB() {
    try {
        // Verificar si el contenedor ya está corriendo
        val checkRunningProcessBuilder = ProcessBuilder("docker", "ps", "-q", "-f", "name=$DATA_BASE_NAME")
        val checkRunningProcess = checkRunningProcessBuilder.start()
        val runningOutput = checkRunningProcess.inputStream.bufferedReader().readText().trim()

        if (runningOutput.isNotEmpty()) {
            println("El contenedor PostgreSQL ya está corriendo.")
            return
        }

        // Si no está corriendo, verificar si existe pero está detenido
        val checkExistsProcessBuilder = ProcessBuilder("docker", "ps", "-aq", "-f", "name=$DATA_BASE_NAME")
        val checkExistsProcess = checkExistsProcessBuilder.start()
        val existsOutput = checkExistsProcess.inputStream.bufferedReader().readText().trim()

        if (existsOutput.isNotEmpty()) {
            // El contenedor existe pero está detenido, iniciarlo
            println("Iniciando contenedor PostgreSQL existente...")
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
        println("🐳 Creando nuevo contenedor PostgreSQL...")
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
            println("Error al crear el contenedor: $output")
            exitProcess(1)
        }
    } catch (e: Exception) {
        println("Error al gestionar PostgreSQL: ${e.message}")
        exitProcess(1)
    }
}

fun waitForDatabase() {
    println("⏳ Esperando a que PostgreSQL esté listo...")
    val maxAttempts = 30
    var attempts = 0
    val connectionUrl = "jdbc:postgresql://localhost:${DATA_BASE_PORT}/${DATA_BASE_NAME}"

    while (attempts < maxAttempts) {
        try {
            // Intentar conexión directa
            DriverManager.getConnection(
                connectionUrl,
                DATA_BASE_USERNAME,
                DATA_BASE_PASSWORD
            ).use { connection ->
                // Intentar una consulta simple
                connection.createStatement().use { statement ->
                    statement.executeQuery("SELECT 1").use { resultSet ->
                        if (resultSet.next()) {
                            println("✅ PostgreSQL está listo y funcionando!")
                            return
                        }
                    }
                }
            }
        } catch (e: Exception) {
            attempts++
            if (attempts < maxAttempts) {
                println("⏳ Intento $attempts/$maxAttempts - PostgreSQL aún no está listo, esperando 2 segundos...")
                Thread.sleep(2000)
            } else {
                println("❌ Error: PostgreSQL no respondió después de $maxAttempts intentos")
                println("❌ Último error: ${e.message}")

                // Mostrar logs del contenedor para debug
                showDockerLogs()
                exitProcess(1)
            }
        }
    }
}

fun showDockerLogs() {
    try {
        println("📋 Mostrando logs del contenedor PostgreSQL:")
        val logsProcessBuilder = ProcessBuilder("docker", "logs", "--tail", "20", DATA_BASE_NAME)
        val logsProcess = logsProcessBuilder.start()

        // Mostrar tanto stdout como stderr
        val output = logsProcess.inputStream.bufferedReader().readText()
        val errorOutput = logsProcess.errorStream.bufferedReader().readText()

        if (output.isNotEmpty()) {
            println("📋 STDOUT:")
            println(output)
        }

        if (errorOutput.isNotEmpty()) {
            println("📋 STDERR:")
            println(errorOutput)
        }

        logsProcess.waitFor()
    } catch (e: Exception) {
        println("❌ Error al obtener logs del contenedor: ${e.message}")
    }
}