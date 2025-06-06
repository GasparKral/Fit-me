package es.gaspardev.modules.shockets

import io.ktor.serialization.kotlinx.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration

fun Application.status() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.parse("15s")
        timeout = Duration.parse("5s")
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/status") {
            sendSerialized("testing")
            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
        }
    }
}