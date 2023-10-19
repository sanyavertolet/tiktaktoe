package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.messages.Notifications

abstract class User<O : Any>(
    val name: String,
    val origin: O,
) {
    abstract suspend fun sendMessage(message: String)

    abstract suspend fun sendNotification(notification: Notifications)

    abstract suspend fun disconnect(reasonMessage: String)
}
