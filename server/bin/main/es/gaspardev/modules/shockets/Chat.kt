package es.gaspardev.modules.shockets

import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.database.daos.ComunicationDao
import es.gaspardev.database.entities.ConversationEntity
import es.gaspardev.enums.MessageStatus
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.time.Duration


fun Application.socket() {
  install(WebSockets) {
    contentConverter = KotlinxWebsocketSerializationConverter(Json)
    pingPeriod = Duration.parse("15s")
    timeout = Duration.parse("5s")
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }
  val communicationDao = ComunicationDao()
  val activeSessions = Collections.synchronizedMap(mutableMapOf<Int, WebSocketSession>())

  routing {
    webSocket("/chat/{userId}/{conversationId}") {
      val userId = call.parameters["userId"]?.toInt() ?: return@webSocket
      val conversationId = call.parameters["conversationId"]?.toIntOrNull() ?: return@webSocket

      // Add the session to active sessions
      activeSessions[userId] = this

      try {
        // Handle incoming messages
        incoming.consumeEach { frame ->
          if (frame is Frame.Text) {
            val receivedText = frame.readText()
            val message = Json.decodeFromString<Message>(receivedText)

            // Store message in database
            val messageEntity = communicationDao.createMessage {
              this.conversation = ConversationEntity[conversationId]
              this.userName = message.userName
              this.sentAt = message.sendAt
              this.content = message.content
              this.status = message.messageStatus
            }

            // Get conversation from database
            val conversation = communicationDao.getChat(conversationId).toModel()

            // Determine recipient ID
            val recipientId = if (userId == conversation.trainer.id) {
              conversation.athlete.id
            } else {
              conversation.trainer.id
            }

            // Send message to recipient if they're connected
            activeSessions[recipientId]?.send(Frame.Text(receivedText))

            // Update message status to DELIVERED if recipient is online
            if (activeSessions.containsKey(recipientId)) {
              messageEntity.status = MessageStatus.DELIVERED
            }
          }
        }
      } finally {
        activeSessions.remove(userId)
      }
    }
  }
}

// Extension function to simplify sending serialized messages
suspend fun WebSocketSession.sendSerialized(message: Message) {
  send(Frame.Text(Json.encodeToString(message)))
}


