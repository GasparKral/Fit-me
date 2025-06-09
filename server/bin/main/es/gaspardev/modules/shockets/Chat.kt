package es.gaspardev.modules.shockets

import es.gaspardev.core.domain.entities.comunication.Conversation
import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.core.domain.entities.users.info.UserSession
import es.gaspardev.core.infrastructure.shockets.WebSocketMessage
import es.gaspardev.database.daos.CommunicationDao
import es.gaspardev.enums.MessageStatus
import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.time.Duration

val jsonSerializer = Json {
    ignoreUnknownKeys = true
    isLenient = true
    classDiscriminator = "type"
    encodeDefaults = true
    serializersModule = kotlinx.serialization.modules.SerializersModule {
        polymorphic(WebSocketMessage::class) {
            subclass(WebSocketMessage.SendMessage::class)
            subclass(WebSocketMessage.MessageReceived::class)
            subclass(WebSocketMessage.MessageStatusUpdate::class)
            subclass(WebSocketMessage.TypingIndicator::class)
            subclass(WebSocketMessage.UserOnlineStatus::class)
            subclass(WebSocketMessage.Error::class)
            subclass(WebSocketMessage.Ping::class)
            subclass(WebSocketMessage.Pong::class)
        }
    }
}

fun Application.chat() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(jsonSerializer)
        pingPeriod = Duration.parse("15s")
        timeout = Duration.parse("30s")
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    val communicationDao = CommunicationDao
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
                send(Frame.Text(jsonSerializer.encodeToString(WebSocketMessage.Pong as WebSocketMessage)))

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
            send(Frame.Text(jsonSerializer.encodeToString(WebSocketMessage.Pong as WebSocketMessage)))
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

        // Verificar si el destinatario está online
        val recipientSessionKey = "${receiverId}_${conversationId}"
        val isRecipientOnline = activeSessions.containsKey(recipientSessionKey)
        val messageId = wsMessage.tempId!!

        // **FIX 1: Guardar mensaje en una sola transacción**
        val savedMessage = transaction {
            try {
                // Crear mensaje en base de datos
                val messageEntity = communicationDao.createMessage(
                    messageId = messageId,
                    conversationId = conversationId,
                    senderId = userId,
                    receiverId = receiverId,
                    content = wsMessage.content,
                    messageType = wsMessage.messageType
                )

                // **FIX 2: Actualizar estado en la misma transacción si es necesario**
                if (isRecipientOnline) {
                    communicationDao.updateMessageStatus(messageId, MessageStatus.DELIVERED, now)
                }

                // Convertir a modelo
                Message(
                    id = messageId,
                    conversationId = conversationId,
                    senderId = userId,
                    senderName = senderName,
                    receiverId = receiverId,
                    content = wsMessage.content,
                    messageType = wsMessage.messageType,
                    sentAt = now,
                    messageStatus = if (isRecipientOnline) MessageStatus.DELIVERED else MessageStatus.SENT,
                    deliveredAt = if (isRecipientOnline) now else null
                )
            } catch (e: Exception) {
                println("Error saving message: ${e.message}")
                e.printStackTrace()
                throw e
            }
        }

        // **FIX 3: Enviar confirmación al remitente primero**
        send(
            Frame.Text(
                jsonSerializer.encodeToString(
                    WebSocketMessage.MessageReceived(savedMessage) as WebSocketMessage
                )
            )
        )

        // **FIX 4: Enviar al destinatario si está online**
        if (isRecipientOnline) {
            try {
                activeSessions[recipientSessionKey]?.session?.send(
                    Frame.Text(
                        jsonSerializer.encodeToString(
                            WebSocketMessage.MessageReceived(savedMessage) as WebSocketMessage
                        )
                    )
                )
            } catch (e: Exception) {
                println("Failed to send message to recipient: ${e.message}")
                // Si falla el envío, actualizar estado a SENT
                transaction {
                    communicationDao.updateMessageStatus(messageId, MessageStatus.SENT, now)
                }
            }
        }

        println("Message saved successfully: messageId=$messageId, conversationId=$conversationId, status=${savedMessage.messageStatus}")

    } catch (e: Exception) {
        println("Failed to handle send message: ${e.message}")
        e.printStackTrace()
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
        // **FIX 5: Usar transacción para actualización de estado**
        val success = transaction {
            communicationDao.updateMessageStatus(wsMessage.messageId, wsMessage.status, wsMessage.timestamp)
        }

        if (success) {
            // Notificar al remitente del mensaje
            val message = transaction {
                communicationDao.getMessage(wsMessage.messageId)
            }

            val senderSessionKey = "${message.sender.id.value}_${conversationId}"
            activeSessions[senderSessionKey]?.session?.send(
                Frame.Text(jsonSerializer.encodeToString(wsMessage as WebSocketMessage))
            )
        }
    } catch (e: Exception) {
        println("Failed to update message status: ${e.message}")
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
                        jsonSerializer.encodeToString(wsMessage as WebSocketMessage)
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
    // **FIX 6: Actualizar estado en base de datos**
    try {
        transaction {
            communicationDao.updateUserOnlineStatus(userId, isOnline)
        }
    } catch (e: Exception) {
        println("Failed to update user online status: ${e.message}")
    }

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
                        jsonSerializer.encodeToString(statusMessage as WebSocketMessage)
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
        val json = Json.encodeToString<WebSocketMessage>(errorMessage)
        send(Frame.Text(json))
    } catch (e: Exception) {
        println("Error enviando mensaje de error: ${e.message}")
        close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, "Serialization error"))
    }
}