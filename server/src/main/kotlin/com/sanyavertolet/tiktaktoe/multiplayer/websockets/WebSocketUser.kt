package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.multiplayer.User
import com.sanyavertolet.tiktaktoe.multiplayer.messages.Requests
import io.ktor.websocket.*
import kotlinx.serialization.json.Json

class WebSocketUser(
    name: String,
    webSocketSession: WebSocketSession,
) : User<WebSocketSession>(name, webSocketSession) {
    override suspend fun sendMessage(message: String) = origin.send(message)

    override suspend fun waitForResponse(): Requests.Turn {
        val response = origin.incoming.receive() as? Frame.Text ?: throw IllegalStateException()
        return Json.decodeFromString(Requests.Turn.serializer(), response.readText())
    }

    override suspend fun disconnect(reasonMessage: String) {
        origin.close(CloseReason(CloseReason.Codes.NORMAL, reasonMessage))
    }
}
