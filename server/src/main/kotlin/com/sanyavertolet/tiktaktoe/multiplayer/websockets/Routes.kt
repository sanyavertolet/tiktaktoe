package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.multiplayer.RequestProcessor
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

private val webSocketLogger = LoggerFactory.getLogger("webSocketLogger")

fun Routing.gameRoute(
    requestProcessor: RequestProcessor<DefaultWebSocketSession> = WebSocketRequestProcessor(),
) = webSocket("/game") {
    try {
        for (frame in incoming) {
            val message = frame.asTextOrError() ?: continue
            val messageText = message.readText()
            webSocketLogger.trace("Receiving $messageText")
            requestProcessor.onRequest(Json.decodeFromString<Requests>(messageText), this)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

suspend fun DefaultWebSocketSession.sendError(notification: Notifications.Error) {
    outgoing.send(Frame.Text(Json.encodeToString(notification as Notifications)))
}

fun Frame.asTextOrError() = this as? Frame.Text
