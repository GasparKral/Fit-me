package es.gaspardev.modules.endpoints

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.io.File

fun Application.upload() {
    routing {
        post("/upload") {
            val multipart = call.receiveMultipart()
            val uploadDir = File("./bucket") // Carpeta donde se guardarán los archivos

            if (!uploadDir.exists()) {
                throw RuntimeException("Error al encontrar la carpeta bucket")
            }

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val fileExtension = part.originalFileName?.substringAfterLast(".") ?: "dat"
                        val fileName = "${System.currentTimeMillis()}.$fileExtension"
                        val file = File(uploadDir, fileName)
                        part.provider().copyAndClose(file.writeChannel())

                        call.respond(
                            HttpStatusCode.OK, "File correctly save: ${file.name}"
                        )
                    }

                    else -> {
                        part.dispose
                        call.respond(HttpStatusCode.BadRequest)
                    }
                }
            }
        }
    }
}