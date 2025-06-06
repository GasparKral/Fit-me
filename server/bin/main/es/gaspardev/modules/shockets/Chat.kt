package es.gaspardev.modules.shockets

import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.core.domain.entities.users.info.UserSession
import es.gaspardev.core.infrastructure.shockets.WebSocketMessage
import es.gaspardev.database.daos.CommunicationDao
import es.gaspardev.database.entities.MessageEntity
import es.gaspardev.enums.MessageStatus
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import java.util.*
import kotlin.time.Duration

val jsonSerializer = Json {
    ignoreUnknownKeys = true
    isLenient = true
    classDiscriminator = "type"
    encodeDefaults = true
}

fun Application.chat() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json {
            ignoreUnknownKeys = true
            isLenient = true
            classDiscriminator = "type"
            encodeDefaults = true
        })
        pingPeriod = Duration.parse("15s")
        timeout = Duration.parse("30s")
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val communicationDao = CommunicationDao()
    val activeSessions = Collections.synchronizedMap(mutableMapOf<String, UserSession>())

    routing {
        webSocket("/chat/{userId}/{conversationId}") {
            val userId = call.parameters["userId"]?.toInt() ?: return@webSocket
            val conversationId = call.parameters["conversationId"]?.toIntOrNull() ?: return@webSocket
            val sessionKey = "${userId}_${conversationId}"

            // Verificar que el usuario tenga acceso a esta conversación
            val conversation = try {
                communicationDao.getChatWith(userId, conversationId)
            } catch (e: Exception) {
                sendError("CONVERSATION_NOT_FOUND", "Conversation not found")
                return@webSocket
            }

            // Verificar permisos
            if (conversation.trainer.id != userId && conversation.athlete.id != userId) {
                sendError("PERMISSION_DENIED", "Access denied to this conversation")
                return@webSocket
            }

            // Registrar sesión
            activeSessions[sessionKey] = UserSession(userId, this, conversationId)

            // Notificar que el usuario está online
            notifyUserOnlineStatus(userId, conversationId, true, activeSessions, communicationDao)

            try {
                // Enviar pong inicial para confirmar conexión
                send(Frame.Text(jsonSerializer.encodeToString<WebSocketMessage.Pong>(WebSocketMessage.Pong)))

                incoming.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            try {
                                val wsMessage = jsonSerializer.decodeFromString<WebSocketMessage>(frame.readText())
                                handleWebSocketMessage(
                                    wsMessage,
                                    userId,
                                    conversationId,
                                    activeSessions,
                                    communicationDao,
                                    conversation
                                )
                            } catch (e: Exception) {
                                sendError("INVALID_MESSAGE", "Invalid message format: ${e.message}")
                            }
                        }

                        is Frame.Ping -> {
                            send(Frame.Pong(frame.data))
                        }

                        else -> {
                            // Ignore other frame types
                        }
                    }
                }
            } catch (e: Exception) {
                println("WebSocket error for user $userId in conversation $conversationId: ${e.message}")
            } finally {
                // Limpiar sesión
                activeSessions.remove(sessionKey)
                // Notificar que el usuario está offline
                notifyUserOnlineStatus(userId, conversationId, false, activeSessions, communicationDao)
            }
        }
    }
}

suspend fun WebSocketSession.handleWebSocketMessage(
    wsMessage: WebSocketMessage,
    userId: Int,
    conversationId: Int,
    activeSessions: MutableMap<String, UserSession>,
    communicationDao: CommunicationDao,
    conversation: Conversation
) {
    when (wsMessage) {
        is WebSocketMessage.SendMessage -> {
            handleSendMessage(wsMessage, userId, conversationId, activeSessions, communicationDao, conversation)
        }

        is WebSocketMessage.MessageStatusUpdate -> {
            handleMessageStatusUpdate(wsMessage, userId, conversationId, activeSessions, communicationDao)
        }

        is WebSocketMessage.TypingIndicator -> {
            handleTypingIndicator(wsMessage, userId, activeSessions)
        }

        is WebSocketMessage.Ping -> {
            send(Frame.Text(jsonSerializer.encodeToString<WebSocketMessage.Pong>(WebSocketMessage.Pong)))
        }

        else -> {
            sendError("UNSUPPORTED_MESSAGE", "Message type not supported")
        }
    }
}

suspend fun WebSocketSession.handleSendMessage(
    wsMessage: WebSocketMessage.SendMessage,
    userId: Int,
    conversationId: Int,
    activeSessions: MutableMap<String, UserSession>,
    communicationDao: CommunicationDao,
    conversation: Conversation
) {
    try {
        val now = Clock.System.now()
        val messageId = UUID.randomUUID().toString()

        // Determinar destinatario
        val receiverId = if (userId == conversation.trainer.id) {
            conversation.athlete.id
        } else {
            conversation.trainer.id
        }

        val senderName = if (userId == conversation.trainer.id) {
            conversation.trainer.fullname
        } else {
            conversation.athlete.fullname
        }

        // Crear mensaje
        val message = Message(
            id = messageId,
            conversationId = conversationId,
            senderId = userId,
            senderName = senderName,
            receiverId = receiverId,
            content = wsMessage.content,
            messageType = wsMessage.messageType,
            sentAt = now,
            messageStatus = MessageStatus.SENT
        )

        // Guardar en base de datos
        val messageEntity: MessageEntity = communicationDao.createMessage(
            messageId = message.id,
            conversationId = message.conversationId,
            senderId = message.senderId,
            receiverId = message.receiverId,
            content = message.content,
            messageType = message.messageType
        )

        // Verificar si el destinatario está online
        val recipientSessionKey = "${receiverId}_${conversationId}"
        val isRecipientOnline = activeSessions.containsKey(recipientSessionKey)

        val finalMessage = if (isRecipientOnline) {
            // Actualizar estado a DELIVERED
            messageEntity.status = MessageStatus.DELIVERED
            messageEntity.deliveredAt = now
            message.copy(
                messageStatus = MessageStatus.DELIVERED,
                deliveredAt = now
            )
        } else {
            message
        }

        // Enviar al destinatario si está online
        if (isRecipientOnline) {
            activeSessions[recipientSessionKey]?.session?.send(
                Frame.Text(
                    jsonSerializer.encodeToString<WebSocketMessage.MessageReceived>(
                        WebSocketMessage.MessageReceived(
                            finalMessage
                        )
                    )
                )
            )
        }

        // Confirmar al remitente
        send(
            Frame.Text(
                jsonSerializer.encodeToString<WebSocketMessage.MessageReceived>(
                    WebSocketMessage.MessageReceived(
                        finalMessage
                    )
                )
            )
        )

    } catch (e: Exception) {
        sendError("SEND_FAILED", "Failed to send message: ${e.message}", wsMessage.tempId)
    }
}

suspend fun WebSocketSession.handleMessageStatusUpdate(
    wsMessage: WebSocketMessage.MessageStatusUpdate,
    userId: Int,
    conversationId: Int,
    activeSessions: MutableMap<String, UserSession>,
    communicationDao: CommunicationDao
) {
    try {
        // Actualizar estado en base de datos
        communicationDao.updateMessageStatus(wsMessage.messageId, wsMessage.status, wsMessage.timestamp)

        // Notificar al remitente del mensaje
        val message = communicationDao.getMessage(wsMessage.messageId)
        val senderSessionKey = "${message.sender.id.value}_${conversationId}"

        activeSessions[senderSessionKey]?.session?.send(
            Frame.Text(jsonSerializer.encodeToString<WebSocketMessage.MessageStatusUpdate>(wsMessage))
        )
    } catch (e: Exception) {
        sendError("STATUS_UPDATE_FAILED", "Failed to update message status: ${e.message}")
    }
}

suspend fun WebSocketSession.handleTypingIndicator(
    wsMessage: WebSocketMessage.TypingIndicator,
    userId: Int,
    activeSessions: MutableMap<String, UserSession>
) {
    // Encontrar otros usuarios en la conversación y notificarles
    activeSessions.values
        .filter { it.conversationId == wsMessage.conversationId && it.userId != userId }
        .forEach { userSession ->
            try {
                userSession.session.send(
                    Frame.Text(
                        jsonSerializer.encodeToString<WebSocketMessage.TypingIndicator>(
                            wsMessage
                        )
                    )
                )
            } catch (e: Exception) {
                // Session might be closed, ignore
            }
        }
}

suspend fun notifyUserOnlineStatus(
    userId: Int,
    conversationId: Int,
    isOnline: Boolean,
    activeSessions: MutableMap<String, UserSession>,
    communicationDao: CommunicationDao
) {
    val statusMessage = WebSocketMessage.UserOnlineStatus(
        userId = userId,
        isOnline = isOnline,
        lastSeen = if (!isOnline) Clock.System.now() else null
    )

    // Notificar a otros usuarios en la misma conversación
    activeSessions.values
        .filter { it.conversationId == conversationId && it.userId != userId }
        .forEach { userSession ->
            try {
                userSession.session.send(
                    Frame.Text(
                        jsonSerializer.encodeToString<WebSocketMessage.UserOnlineStatus>(
                            statusMessage
                        )
                    )
                )
            } catch (e: Exception) {
                // Session might be closed, ignore
            }
        }
}

suspend fun WebSocketSession.sendError(code: String, message: String, tempId: String? = null) {
    try {
        val errorMessage = WebSocketMessage.Error(code, message, tempId)
        val json = Json.encodeToString<WebSocketMessage>(errorMessage) // ← Changed this line
        send(Frame.Text(json))
    } catch (e: Exception) {
        println("Error enviando mensaje de error: ${e.message}")
        close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Serialization error"))
    }
}