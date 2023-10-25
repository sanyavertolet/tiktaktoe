package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.User
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WebSocketUser(
    name: String,
    webSocketSession: DefaultWebSocketSession,
) : User<DefaultWebSocketSession>(name, webSocketSession) {
    override fun isActive(): Boolean = !origin.closeReason.isCompleted

    override suspend fun sendMessage(message: String) = origin.send(message)

    override suspend fun sendNotification(notification: Notifications) = sendMessage(Json.encodeToString(notification))

    override suspend fun disconnect(reasonMessage: String) {
        origin.close(CloseReason(CloseReason.Codes.NORMAL, reasonMessage))
    }
}
