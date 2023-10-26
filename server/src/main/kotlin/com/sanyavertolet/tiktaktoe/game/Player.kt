package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

class Player<O : Any>(
    private val user: User<O>,
    val type: MarkerType,
) {
    val name: String = user.name

    val origin: O = user.origin

    fun isActive() = user.isActive()

    private suspend fun sendMessage(message: String) = user.sendMessage(message)
        .also { logger.trace("Sending to $name: $message") }

    suspend fun sendNotification(notification: Notifications) = sendMessage(Json.encodeToString(notification))

    companion object {
        private val logger = LoggerFactory.getLogger("UserLogger")
    }
}
