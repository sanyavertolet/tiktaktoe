package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.system.exitProcess

class WebSocketClient(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    engine: HttpClientEngineFactory<*> = CIO,
) : Client {
    private val scope = CoroutineScope(coroutineContext)
    private val client = HttpClient(engine) {
        install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(Json) }
    }
    private val messageQueue = Channel<Requests>()

    override suspend fun startSessionAndRequest(
        url: String,
        onNotificationReceived: (Notifications) -> Unit,
        andAction: () -> Requests,
    ) {
        client.webSocket("ws://$url/game") {
            sendSerialized(andAction())

            scope.launch { processIncoming(onNotificationReceived) }
            scope.launch { processOutgoing() }

            println(closeReason.await()?.message)
            exitProcess(0)
        }
    }

    private suspend fun DefaultClientWebSocketSession.processOutgoing() {
        for (message in messageQueue) {
            sendSerialized(message)
        }
    }

    private suspend fun ClientWebSocketSession.processIncoming(onNotificationReceived: (Notifications) -> Unit) {
        for (frame in incoming) {
            val message = (frame as? Frame.Text)?.readText() ?: throw WebSocketException("Could not read text")
            val notification: Notifications = Json.decodeFromString(message)
            onNotificationReceived(notification)
        }
    }

    override fun sendRequest(request: Requests) {
        scope.launch { messageQueue.send(request) }
    }
}
