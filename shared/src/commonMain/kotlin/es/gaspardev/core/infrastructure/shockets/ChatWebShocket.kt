package es.gaspardev.core.infrastructure.shockets

import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
import es.gaspardev.utils.SERVER_ADRESS
import es.gaspardev.utils.SERVER_HOST
import es.gaspardev.utils.SERVER_PORT
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    classDiscriminator = "type"
    encodeDefaults = true
}

class ChatWebSocket(
    private val userId: Int,
    private val conversationId: Int,
    private val onWebSocketMessage: (WebSocketMessage) -> Unit,
    private val onConnectionEvent: (Boolean) -> Unit,
    private val onError: (String) -> Unit = { },
    coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext = coroutineContext + job

    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
            pingInterval = 15.toDuration(DurationUnit.SECONDS)
            maxFrameSize = Long.MAX_VALUE
        }
    }

    private var session: DefaultClientWebSocketSession? = null
    private var isConnected = false
    private var shouldReconnect = true
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5
    private var heartbeatJob: Job? = null

    fun connect() {
        launch {
            connectWithRetry()
        }
    }

    private suspend fun connectWithRetry() {
        while (shouldReconnect && reconnectAttempts < maxReconnectAttempts) {
            try {
                println("🔌 Conectando al WebSocket... (Intento ${reconnectAttempts + 1})")

                client.webSocket(
                    method = HttpMethod.Get,
                    host = SERVER_HOST,
                    port = SERVER_PORT,
                    path = "/chat/$userId/$conversationId"
                ) {
                    session = this
                    isConnected = true
                    reconnectAttempts = 0
                    onConnectionEvent(true)

                    println("✅ WebSocket conectado exitosamente")

                    // Iniciar heartbeat
                    startHeartbeat()

                    // Enviar ping inicial
                    send(Frame.Text(json.encodeToString<WebSocketMessage.Ping>(WebSocketMessage.Ping)))

                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    try {
                                        val messageText = frame.readText()
                                        val wsMessage = json.decodeFromString<WebSocketMessage.Pong>(messageText)

                                        @Suppress("UNUSED_EXPRESSION")
                                        when (wsMessage) {

                                            else -> {
                                                // Keep alive response
                                            }
                                        }
                                    } catch (e: Exception) {
                                        println("❌ Error al procesar mensaje: ${e.message}")
                                        onError("Error parsing message: ${e.message}")
                                    }
                                }

                                is Frame.Ping -> {
                                    send(Frame.Pong(frame.data))
                                }

                                is Frame.Close -> {
                                    println("🔌 Conexión WebSocket cerrada")
                                    handleDisconnection()
                                    return@webSocket
                                }

                                else -> {
                                    println("📦 Frame desconocido recibido: ${frame.frameType}")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("❌ Error en el manejo de mensajes: ${e.message}")
                        onError("Connection error: ${e.message}")
                        handleDisconnection()
                    }
                }

                handleDisconnection()

            } catch (e: Exception) {
                println("❌ Fallo en la conexión: ${e.message}")
                onError("Connection failed: ${e.message}")
                reconnectAttempts++

                if (reconnectAttempts < maxReconnectAttempts && shouldReconnect) {
                    val delay = minOf(2000L * reconnectAttempts, 10000L) // Max 10 segundos
                    println("🔄 Reintentando en ${delay}ms...")
                    delay(delay)
                } else {
                    onError("Max reconnection attempts reached")
                    disconnect()
                    break
                }
            }
        }
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()
        heartbeatJob = CoroutineScope(Dispatchers.IO + SupervisorJob()).launch { // ← Scope independiente
            while (isConnected && session != null) {
                try {
                    delay(30000)
                    if (isConnected && session != null) {
                        session?.send(Frame.Text(json.encodeToString<WebSocketMessage.Ping>(WebSocketMessage.Ping)))
                    }
                } catch (e: CancellationException) {
                    // Normal cancellation, exit quietly
                    break
                } catch (e: Exception) {
                    println("❌ Error en heartbeat: ${e.message}")
                    break
                }
            }
        }
    }

    private suspend fun handleDisconnection() {
        heartbeatJob?.cancel()
        session = null
        isConnected = false
        onConnectionEvent(false)

        // ← Evitar reconexión automática si fue desconexión intencional
        if (shouldReconnect && reconnectAttempts < maxReconnectAttempts && !job.isCancelled) {
            delay(2000)
            reconnectAttempts++
            connectWithRetry()
        }
    }


    suspend fun sendMessage(
        content: String,
        messageType: MessageType = MessageType.TEXT,
        tempId: String? = null
    ): Boolean {
        if (!isConnected || session == null) {
            onError("Not connected to send messages")
            return false
        }

        val message = WebSocketMessage.SendMessage(
            content = content,
            messageType = messageType,
            tempId = tempId
        )

        return try {
            val messageJson = json.encodeToString<WebSocketMessage.SendMessage>(message)
            session?.send(Frame.Text(messageJson))
            true
        } catch (e: Exception) {
            println("❌ Error enviando mensaje: ${e.message}")
            onError("Failed to send message: ${e.message}")
            handleDisconnection()
            false
        }
    }

    suspend fun sendStatusUpdate(messageId: String, status: MessageStatus): Boolean {
        if (!isConnected || session == null) return false

        val statusUpdate = WebSocketMessage.MessageStatusUpdate(
            messageId = messageId,
            status = status,
            timestamp = Clock.System.now()
        )

        return try {
            val messageJson = json.encodeToString<WebSocketMessage.MessageStatusUpdate>(statusUpdate)
            session?.send(Frame.Text(messageJson))
            true
        } catch (e: Exception) {
            println("❌ Error enviando actualización de estado: ${e.message}")
            false
        }
    }

    suspend fun sendTypingIndicator(conversationId: Int, isTyping: Boolean): Boolean {
        if (!isConnected || session == null) return false

        val typingIndicator = WebSocketMessage.TypingIndicator(
            userId = userId,
            conversationId = conversationId,
            isTyping = isTyping
        )

        return try {
            val messageJson = json.encodeToString<WebSocketMessage.TypingIndicator>(typingIndicator)
            session?.send(Frame.Text(messageJson))
            true
        } catch (e: Exception) {
            println("❌ Error enviando indicador de escritura: ${e.message}")
            false
        }
    }

    fun getConnectionStatus(): String {
        return when {
            isConnected && session != null -> "Connected"
            shouldReconnect && reconnectAttempts > 0 -> "Reconnecting..."
            !shouldReconnect -> "Disconnected"
            else -> "Connecting..."
        }
    }

    fun disconnect() {
        shouldReconnect = false
        heartbeatJob?.cancel()

        launch {
            try {
                session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnecting"))
            } catch (e: Exception) {
                println("❌ Error cerrando WebSocket: ${e.message}")
            } finally {
                session = null
                isConnected = false
                onConnectionEvent(false)
                client.close()
                job.cancel()
            }
        }
    }


}
