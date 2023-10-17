package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.exceptions.LobbyException
import com.sanyavertolet.tiktaktoe.game.Player
import com.sanyavertolet.tiktaktoe.game.PlayerType
import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Lobby<O : Any> (
    val host: User<O>,
    val lobbyCode: String = lobbyCounter.incrementAndGet().toString(),
) {
    private var anotherUser: User<O>? = null
    private var boardSize: Int = 3
    private var rowWinCount: Int = 3

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

    suspend fun close(disconnectReason: String = DEFAULT_DISCONNECT_MESSAGE) {
        host.disconnect(disconnectReason)
        anotherUser?.disconnect(disconnectReason)
    }

    companion object {
        val lobbyCounter = AtomicInteger(0)
        val lobbies: MutableSet<Lobby<WebSocketSession>> = mutableSetOf()
        private const val DEFAULT_DISCONNECT_MESSAGE = "Lobby was closed."
    }
}
