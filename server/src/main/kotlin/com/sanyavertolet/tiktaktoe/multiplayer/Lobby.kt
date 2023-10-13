package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.game.Player
import com.sanyavertolet.tiktaktoe.game.PlayerType
import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import java.util.concurrent.atomic.AtomicInteger

class Lobby<O : Any> (
    val host: User<O>,
    val lobbyCode: Int = lobbyCounter.incrementAndGet(),
) {
    private var anotherUser: User<O>? = null
    private var boardSize: Int = 3
    private var rowWinCount: Int = 3

    fun connectUser(user: User<O>) {
        anotherUser = user
    }

    fun getUsers() = listOf(host, anotherUser)

    suspend fun createGame() = anotherUser?.let {
        val players = listOf(
            Player(host, PlayerType.TICK),
            Player(it, PlayerType.CROSS),
        )
        TikTakToeGame(players, boardSize, rowWinCount).run()
    } ?: throw LobbyException()

    suspend fun close() {
        host.disconnect("")
        anotherUser?.disconnect("")
    }

    companion object {
        val lobbyCounter = AtomicInteger(0)
    }
}

class LobbyException(message: String? = null) : Exception(message)
