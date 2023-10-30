package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.games
import com.sanyavertolet.tiktaktoe.lobbies
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

class GarbageCollector(
    context: CoroutineContext = Dispatchers.Default,
) {
    private val gcScope = CoroutineScope(context)

    private fun TikTakToeGame.isActive() = players.all { it.isActive() }

    private fun <S : Any> Lobby<S>.isActive() = users.all { it.isActive() }

    private fun cleanUpGames() {
        games.filter { (_, game) -> !game.isActive() }
            .forEach { (lobbyCode, _) -> games.remove(lobbyCode) }
    }

    private suspend fun cleanUpLobbies() {
        lobbies.filter { lobby -> !lobby.isActive() }
            .forEach {
                it.close()
                lobbies.remove(it)
            }
    }

    private suspend fun cleanUp() {
        cleanUpLobbies()
        cleanUpGames()
    }

    fun run(gcDelay: Duration) {
        gcScope.launch {
            while (true) {
                delay(gcDelay)
                cleanUp()
            }
        }
    }
}
