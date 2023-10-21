package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class Player<O : Any>(
    private val user: User<O>,
    val type: PlayerType,
) {
    val name: String
        get() = user.name

    val origin: O
        get() = user.origin

    private suspend fun sendMessage(message: String) = user.sendMessage(message)
        .also { logger.trace("Sending to $name: $message") }

    suspend fun sendNotification(notification: Notifications) = sendMessage(Json.encodeToString(notification))

    companion object {
        private val logger = LoggerFactory.getLogger("UserLogger")
    }
}

enum class PlayerType {
    TICK,
    CROSS,
    NONE,
}
