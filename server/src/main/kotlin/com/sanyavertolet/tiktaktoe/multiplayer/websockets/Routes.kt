package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.messages.Requests
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

private val webSocketLogger = LoggerFactory.getLogger("webSocketLogger")

fun Routing.gameRoute() = webSocket("/game") {
    val requestProcessor = WebSocketRequestProcessor()
    try {
        for (frame in incoming) {
            val message = frame as? Frame.Text ?: continue
            val messageText = message.readText()
            webSocketLogger.trace("Receiving $messageText")
            requestProcessor.onRequest(Json.decodeFromString<Requests>(messageText), this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
