package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.multiplayer.Notification
import com.sanyavertolet.tiktaktoe.multiplayer.Request
import com.sanyavertolet.tiktaktoe.multiplayer.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Player<O : Any>(
    private val user: User<O>,
    val type: PlayerType,
) {
    val name: String
        get() = user.name

    private suspend fun sendMessage(message: String) = user.sendMessage(message)

    suspend fun waitForTurn() = user.waitForResponse()

    suspend fun sendNotification(
        notification: Notification,
    ) = sendMessage(Json.encodeToString(notification))
}

enum class PlayerType {
    TICK,
    CROSS,
    NONE,
}
