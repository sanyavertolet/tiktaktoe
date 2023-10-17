package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

val client = HttpClient(CIO) {
    install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(Json) }
}

val messageQueue = Channel<Requests>()

val scope = CoroutineScope(Dispatchers.Default)

suspend fun startSessionAndRequest(
    andAction: () -> Requests,
) {
    client.ws("/game") {
        sendSerialized(andAction())

        scope.launch { processIncoming() }
        scope.launch { processOutgoing() }
    }
}

suspend fun DefaultClientWebSocketSession.processOutgoing() {
    for (message in messageQueue) {
        sendSerialized(message)
    }
}

suspend fun ClientWebSocketSession.processIncoming() {
    for (frame in incoming) {
        val message = (frame as? Frame.Text)?.readText() ?: throw WebSocketException("Could not read text")
        val notification: Notifications = Json.decodeFromString(message)
        when (notification) {
            is Notifications.GameStarted -> {
                TODO("Render field")
            }
            is Notifications.GameFinished -> {
                TODO("Show game winner")
            }
            is Notifications.Turn -> {
                TODO("Process turn")
            }
        }
    }
}
