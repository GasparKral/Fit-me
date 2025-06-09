package es.gaspardev.modules.endpoints

import es.gaspardev.database.daos.CommunicationDao
import es.gaspardev.enums.MessageStatus
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.comunication() {

    route("comunication") {

        get("/conversations/{conversationId}") {
            val conversationId = call.parameters["conversationId"]?.toIntOrNull() ?: return@get
            val conversation = CommunicationDao.getChat(conversationId).toModel()
            call.respond(conversation)
        }

        post("/messages/{messageId}/read") {
            val messageId = call.parameters["messageId"] ?: return@post
            CommunicationDao.updateMessageStatus(messageId, MessageStatus.READ)
            call.respond(HttpStatusCode.OK)
        }
    }

}