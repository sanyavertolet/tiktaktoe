package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.exceptions.LobbyException
import com.sanyavertolet.tiktaktoe.game.Player
import com.sanyavertolet.tiktaktoe.game.PlayerType
import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.messages.Notifications
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Lobby<O : Any> (
    val host: User<O>,
    val boardSize: Int,
    val rowWinCount: Int,
    val lobbyCode: String = lobbyCounter.incrementAndGet().toString(),
) {
    private var anotherUser: User<O>? = null

    val users: List<User<O>>
        get() = listOfNotNull(host, anotherUser)

    fun createGame() = anotherUser?.let {
        val players = listOf(
            Player(host, PlayerType.TICK),
            Player(it, PlayerType.CROSS),
        )
        TikTakToeGame(players, boardSize, rowWinCount)
    } ?: throw LobbyException()

    fun connectUser(user: User<O>) {
        anotherUser = user
    }

    suspend fun notifyAll(notifications: Notifications) = listOf(host, anotherUser).forEach {
        it?.sendNotification(notifications)
    }

    suspend fun disconnectUser(userName: String, origin: O) {
        if (host.name == userName && host.origin == origin) {
            close("Host has left")
        } else if (anotherUser?.name == userName && anotherUser?.origin == origin) {
            anotherUser = null
            host.sendNotification(Notifications.PlayerLeft)
        }
    }

    suspend fun close(disconnectReason: String = DEFAULT_DISCONNECT_MESSAGE) {
        try {
            host.disconnect(disconnectReason)
        } finally {
            anotherUser?.disconnect(disconnectReason)
        }
        lobbies.find { it.lobbyCode == lobbyCode }?.let { lobbies.remove(it) }
    }

    companion object {
        val lobbyCounter = AtomicInteger(0)
        val lobbies: MutableSet<Lobby<DefaultWebSocketSession>> = mutableSetOf()
        private const val DEFAULT_DISCONNECT_MESSAGE = "Lobby was closed as the opponent has left."
    }
}
