package es.gaspardev.core.infrastructure.shockets

import es.gaspardev.enums.MessageStatus
import es.gaspardev.enums.MessageType
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
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.coroutines.CoroutineContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    classDiscriminator = "type"
    encodeDefaults = true
    // ✅ Configurar serialización polimórfica
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

class ChatWebSocket(
    private val userId: Int,
    private val conversationId: Int,
    private val onWebSocketMessage: (WebSocketMessage) -> Unit,
    private val onConnectionEvent: (Boolean) -> Unit,
    private val onError: (String) -> Unit = { },
    parentCoroutineContext: CoroutineContext = Dispatchers.IO // ✅ Renamed to avoid confusion
) : CoroutineScope {

    // ✅ FIX: Usar SupervisorJob independiente para evitar cancelaciones
    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext =
        parentCoroutineContext + job + CoroutineName("ChatWebSocket-$userId-$conversationId")

    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(json)
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
        if (isConnected) {
            println("⚠️ WebSocket ya está conectado")
            return
        }

        println("🔌 Iniciando conexión WebSocket...")
        launch {
            connectWithRetry()
        }
    }

    private suspend fun connectWithRetry() {
        while (shouldReconnect && reconnectAttempts < maxReconnectAttempts && !job.isCancelled) {
            try {
                println("🔌 Conectando al WebSocket... (Intento ${reconnectAttempts + 1})")

                // ✅ FIX: Usar timeout y manejo de errores mejorado
                withTimeoutOrNull(30000) { // 30 segundos timeout
                    client.webSocket(
                        method = HttpMethod.Get,
                        host = SERVER_HOST,
                        port = SERVER_PORT,
                        path = "/chat/$userId/$conversationId"
                    ) {
                        if (!job.isActive) {
                            println("⚠️ Job cancelado durante conexión")
                            return@webSocket
                        }

                        session = this
                        isConnected = true
                        reconnectAttempts = 0
                        onConnectionEvent(true)

                        println("✅ WebSocket conectado exitosamente")

                        // Iniciar heartbeat
                        startHeartbeat()

                        // Enviar ping inicial
                        send(Frame.Text(json.encodeToString(WebSocketMessage.Ping as WebSocketMessage)))

                        try {
                            for (frame in incoming) {
                                if (!job.isActive || !isConnected) {
                                    println("⚠️ Saliendo del loop de mensajes: job.isActive=${job.isActive}, isConnected=$isConnected")
                                    break
                                }

                                when (frame) {
                                    is Frame.Text -> {
                                        try {
                                            val messageText = frame.readText()
                                            println("📨 Mensaje recibido del servidor: $messageText")

                                            // **FIX: Deserializar como WebSocketMessage genérico**
                                            val wsMessage = json.decodeFromString<WebSocketMessage>(messageText)

                                            // **FIX: Procesar el mensaje según su tipo**
                                            when (wsMessage) {
                                                is WebSocketMessage.Pong -> {
                                                    println("🏓 Pong recibido del servidor")
                                                    // Keep alive response - no action needed
                                                }

                                                is WebSocketMessage.MessageReceived -> {
                                                    println("✅ Mensaje confirmado del servidor: ${wsMessage.message.id}")
                                                    onWebSocketMessage(wsMessage)
                                                }

                                                is WebSocketMessage.MessageStatusUpdate -> {
                                                    println("📋 Actualización de estado: ${wsMessage.messageId} -> ${wsMessage.status}")
                                                    onWebSocketMessage(wsMessage)
                                                }

                                                is WebSocketMessage.TypingIndicator -> {
                                                    println("⌨️ Indicador de escritura: Usuario ${wsMessage.userId} ${if (wsMessage.isTyping) "escribiendo" else "dejó de escribir"}")
                                                    onWebSocketMessage(wsMessage)
                                                }

                                                is WebSocketMessage.UserOnlineStatus -> {
                                                    println("🟢 Estado de usuario: Usuario ${wsMessage.userId} ${if (wsMessage.isOnline) "online" else "offline"}")
                                                    onWebSocketMessage(wsMessage)
                                                }

                                                is WebSocketMessage.Error -> {
                                                    println("❌ Error del servidor: ${wsMessage.code} - ${wsMessage.message}")
                                                    onError("Server error: ${wsMessage.message}")
                                                    onWebSocketMessage(wsMessage)
                                                }

                                                else -> {
                                                    println("📦 Mensaje no reconocido: ${wsMessage::class.simpleName}")
                                                    onWebSocketMessage(wsMessage)
                                                }
                                            }

                                        } catch (e: Exception) {
                                            println("❌ Error al procesar mensaje: ${e.message}")
                                            println("📝 Mensaje original: ${frame.readText()}")
                                            onError("Error parsing message: ${e.message}")
                                        }
                                    }

                                    is Frame.Ping -> {
                                        println("🏓 Ping recibido del servidor, enviando Pong")
                                        send(Frame.Pong(frame.data))
                                    }

                                    is Frame.Close -> {
                                        println("🔌 Conexión WebSocket cerrada por el servidor")
                                        handleDisconnection()
                                        return@webSocket
                                    }

                                    else -> {
                                        println("📦 Frame desconocido recibido: ${frame.frameType}")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            if (e is CancellationException) {
                                println("⚠️ Conexión WebSocket cancelada")
                                return@webSocket
                            }
                            println("❌ Error en el manejo de mensajes: ${e.message}")
                            e.printStackTrace()
                            onError("Connection error: ${e.message}")
                            handleDisconnection()
                        }
                    }
                } ?: run {
                    println("⏰ Timeout al conectar WebSocket")
                    throw Exception("Connection timeout")
                }

                handleDisconnection()

            } catch (e: Exception) {
                if (e is CancellationException) {
                    println("⚠️ Conexión cancelada por el usuario")
                    break
                }

                println("❌ Fallo en la conexión: ${e.message}")
                e.printStackTrace()
                onError("Connection failed: ${e.message}")
                reconnectAttempts++

                if (reconnectAttempts < maxReconnectAttempts && shouldReconnect && job.isActive) {
                    val delay = minOf(2000L * reconnectAttempts, 10000L) // Max 10 segundos
                    println("🔄 Reintentando en ${delay}ms...")
                    delay(delay)
                } else {
                    println("❌ Máximo de intentos de reconexión alcanzado o job cancelado")
                    onError("Max reconnection attempts reached")
                    disconnect()
                    break
                }
            }
        }
    }

    private fun startHeartbeat() {
        heartbeatJob?.cancel()

        // ✅ FIX: Usar scope del WebSocket para evitar conflictos
        heartbeatJob = launch {
            while (isConnected && session != null && isActive) {
                try {
                    delay(30000)
                    if (isConnected && session != null && isActive) {
                        println("💓 Enviando heartbeat")
                        session?.send(Frame.Text(json.encodeToString(WebSocketMessage.Ping as WebSocketMessage)))
                    }
                } catch (e: CancellationException) {
                    println("💓 Heartbeat cancelado")
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
            // **FIX: Enviar con el tipo correcto**
            val messageJson = json.encodeToString(message as WebSocketMessage)
            println("📤 Enviando mensaje: $content (tempId: $tempId)")
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
            val messageJson = json.encodeToString(statusUpdate as WebSocketMessage)
            println("📋 Enviando actualización de estado: $messageId -> $status")
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
            val messageJson = json.encodeToString(typingIndicator as WebSocketMessage)
            println("⌨️ Enviando indicador de escritura: $isTyping")
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
        println("🔌 Desconectando WebSocket...")
        shouldReconnect = false
        heartbeatJob?.cancel()

        launch {
            try {
                isConnected = false
                session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnecting"))
            } catch (e: Exception) {
                println("❌ Error cerrando WebSocket: ${e.message}")
            } finally {
                session = null
                onConnectionEvent(false)
                try {
                    client.close()
                } catch (e: Exception) {
                    println("❌ Error cerrando cliente HTTP: ${e.message}")
                }
                job.cancel()
                println("✅ WebSocket desconectado completamente")
            }
        }
    }
}