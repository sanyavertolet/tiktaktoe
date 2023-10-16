package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Player<O : Any>(
    private val user: User<O>,
    val type: PlayerType,
) {
    val name: String
        get() = user.name

    val origin: O
        get() = user.origin

    private suspend fun sendMessage(message: String) = user.sendMessage(message)

    suspend fun sendNotification(notification: Notifications) = sendMessage(Json.encodeToString(notification))
}

enum class PlayerType {
    TICK,
    CROSS,
    NONE,
}
