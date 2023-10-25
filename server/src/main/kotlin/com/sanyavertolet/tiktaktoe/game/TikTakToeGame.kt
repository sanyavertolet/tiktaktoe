package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.messages.Notifications
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

typealias Result = Pair<Boolean, PlayerType>

class TikTakToeGame(
    val players: List<Player<*>>,
    boardSize: Int,
    rowWinCount: Int,
) {
    private val field: Field = Field(boardSize, rowWinCount)
    private val whoseTurn = AtomicInteger(0)

    private fun whoWins(): Result = field.whoWins()

    private suspend fun notifyBegin() = sendAll(Notifications.GameStarted(currentTurnPlayer.name))

    suspend fun notifyEnd(winner: Player<*>?) = sendAll(Notifications.GameFinished(winner?.name))

    suspend fun turn(position: Position): Position? {
        field[position] = currentTurnPlayer
        val (isFinished, winner) = whoWins()
        return if (isFinished) {
            notifyEnd(players.find { it.type == winner })
            null
        } else {
            whoseTurn.incrementAndGet()
            position
        }
    }

    val currentTurnPlayer: Player<*>
        get() = players[whoseTurn.get() % 2]

    val previousTurnPlayer: Player<*>
        get() = players[(whoseTurn.get() + 1) % 2]

    suspend fun sendAll(notification: Notifications) {
        players.forEach { it.sendNotification(notification) }
    }

    suspend fun run() = notifyBegin().also { whoseTurn.set(0) }

    companion object {
        val games: ConcurrentHashMap<String, TikTakToeGame> = ConcurrentHashMap()
    }
}
