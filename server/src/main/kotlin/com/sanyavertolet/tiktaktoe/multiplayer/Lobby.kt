package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.LobbyDto
import com.sanyavertolet.tiktaktoe.exceptions.GameException
import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Player
import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.lobbies
import com.sanyavertolet.tiktaktoe.messages.Notifications
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
            Player(host, MarkerType.TIC),
            Player(it, MarkerType.TAC),
        )
        TikTakToeGame(players, boardSize, rowWinCount)
    } ?: throw GameException("User was not found", false)

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

    fun toDto() = LobbyDto(
        host.name,
        Options(boardSize, rowWinCount),
        lobbyCode,
    )

    companion object {
        val lobbyCounter = AtomicInteger(0)
        private const val DEFAULT_DISCONNECT_MESSAGE = "Lobby was closed as the opponent has left."
    }
}
