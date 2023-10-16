package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.multiplayer.messages.Requests

abstract class User<O : Any>(
    val name: String,
    val origin: O,
) {
    abstract suspend fun waitForResponse(): Requests.Turn

    abstract suspend fun sendMessage(message: String)

    abstract suspend fun disconnect(reasonMessage: String)
}
