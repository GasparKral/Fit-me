package es.gaspardev.modules.endpoints

import es.gaspardev.database.daos.ComunicationDao
import es.gaspardev.enums.MessageStatus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.comunication() {

    routing {

        get("/conversations/{conversationId}") {
            val conversationId = call.parameters["conversationId"]?.toIntOrNull() ?: return@get
            val conversation = ComunicationDao().getChat(conversationId).toModel()
            call.respond(conversation)
        }

        post("/messages/{messageId}/read") {
            val messageId = call.parameters["messageId"]?.toIntOrNull() ?: return@post
            ComunicationDao().updateMessageStatus(messageId, MessageStatus.READ)
            call.respond(HttpStatusCode.OK)
        }
    }

}