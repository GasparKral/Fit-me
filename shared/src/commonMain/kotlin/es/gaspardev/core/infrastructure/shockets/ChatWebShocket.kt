package es.gaspardev.core.infrastructure.shockets

import es.gaspardev.core.domain.entities.comunication.Message
import es.gaspardev.enums.MessageStatus
import es.gaspardev.utils.SERVER_ADRESS
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

class ChatWebShocket(
    private val userId: String,
    private val conversationId: Int,
    private val onMessageReceived: (Message) -> Unit,
    private val onStatusUpdate: (MessageStatus) -> Unit,
    private val onConnectionEvent: (Boolean) -> Unit,
    private val onError: (String) -> Unit = { },
    coroutineContext: CoroutineContext = Dispatchers.IO
) : CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext = coroutineContext + job

    private val client = HttpClient {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
            pingInterval = 15.toDuration(DurationUnit.SECONDS)
            maxFrameSize = Long.MAX_VALUE
        }
    }

    private var session: DefaultClientWebSocketSession? = null
    private var isActive = false
    private var shouldReconnect = true
    private var reconnectAttempts = 0
    private val maxReconnectAttempts = 5

    fun connect() {
        launch {
            connectWithRetry()
        }
    }

    private suspend fun connectWithRetry() {
        while (shouldReconnect && reconnectAttempts < maxReconnectAttempts) {
            try {
                println("Attempting to connect to WebSocket... (Attempt ${reconnectAttempts + 1})")

                client.webSocket(
                    method = HttpMethod.Get,
                    host = SERVER_ADRESS,
                    port = SERVER_PORT,
                    path = "/chat/$userId/$conversationId"
                ) {
                    session = this
                    this@ChatWebShocket.isActive = true
                    reconnectAttempts = 0 // Reset on successful connection
                    onConnectionEvent(true)

                    println("WebSocket connected successfully")

                    // Listen for incoming messages
                    try {
                        for (frame in incoming) {
                            when (frame) {
                                is Frame.Text -> {
                                    try {
                                        val messageText = frame.readText()
                                        println("Received message: $messageText")
                                        val message = Json.decodeFromString<Message>(messageText)
                                        onMessageReceived(message)
                                    } catch (e: Exception) {
                                        onError("Error parsing message: ${e.message}")
                                    }
                                }

                                is Frame.Binary -> {
                                    // Handle binary data if needed
                                    println("Received binary frame")
                                }

                                is Frame.Close -> {
                                    println("WebSocket connection closed")
                                    handleDisconnection()
                                    return@webSocket
                                }

                                else -> {
                                    println("Received unknown frame type: ${frame.frameType}")
                                }
                            }
                        }
                    } catch (e: Exception) {
                        println("Error in message handling: ${e.message}")
                        onError("Connection error: ${e.message}")
                        handleDisconnection()
                    }
                }

                // If we reach here, connection was closed
                handleDisconnection()

            } catch (e: Exception) {
                println("Connection failed: ${e.message}")
                onError("Connection failed: ${e.message}")
                reconnectAttempts++

                if (reconnectAttempts < maxReconnectAttempts && shouldReconnect) {
                    val delay = minOf(1000L * reconnectAttempts, 10000L) // Max 10 seconds
                    println("Retrying in ${delay}ms...")
                    delay(delay)
                } else {
                    onError("Max reconnection attempts reached")
                    closeConnection()
                    break
                }
            }
        }
    }

    private suspend fun handleDisconnection() {
        session = null
        isActive = false
        onConnectionEvent(false)

        if (shouldReconnect && reconnectAttempts < maxReconnectAttempts) {
            delay(1000) // Wait before reconnecting
            reconnectAttempts++
            connectWithRetry()
        }
    }

    suspend fun sendMessage(content: String): Boolean {
        if (!isActive || session == null) {
            onError("Not connected to send messages")
            return false
        }

        val message = Message(
            userName = userId,
            sendAt = Clock.System.now(),
            content = content,
            messageStatus = MessageStatus.SENT
        )

        try {
            val messageJson = Json.encodeToString(message)
            println("Sending message: $messageJson")
            session?.send(Frame.Text(messageJson))

            // Add message to UI immediately with SENT status
            onMessageReceived(message)
            return true
        } catch (e: Exception) {
            println("Error sending message: ${e.message}")
            onError("Failed to send message: ${e.message}")
            handleDisconnection()
            return false
        }
    }

    // Helper function to check connection status
    fun getConnectionStatus(): String {
        return when {
            isActive && session != null -> "Connected"
            shouldReconnect && reconnectAttempts > 0 -> "Reconnecting..."
            !shouldReconnect -> "Disconnected"
            else -> "Connecting..."
        }
    }

    fun closeConnection() {
        shouldReconnect = false

        launch {
            try {
                session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client closing"))
            } catch (e: Exception) {
                println("Error closing WebSocket: ${e.message}")
            } finally {
                session = null
                this@ChatWebShocket.isActive = false
                onConnectionEvent(false)
            }
        }
    }

    fun reconnect() {
        if (!shouldReconnect) {
            shouldReconnect = true
            reconnectAttempts = 0
        }
        connect()
    }

    fun isConnected(): Boolean = isActive && session != null

    fun dispose() {
        closeConnection()
        client.close()
    }
}