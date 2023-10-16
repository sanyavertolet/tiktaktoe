package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.messages.Notifications
import java.util.concurrent.atomic.AtomicInteger

typealias Result = Pair<Boolean, PlayerType>

class TikTakToeGame(
    private val players: List<Player<*>>,
    boardSize: Int = 3,
    rowWinCount: Int = 3,
) {
    private val field: Field = Field(boardSize, rowWinCount)
    private val whoseTurn = AtomicInteger(0)

    private fun whoWins(): Result = field.whoWins()

    private suspend fun notifyBegin() {
        val gameStarted: Notifications = Notifications.GameStarted(players.first().name)
        players.forEach { it.sendNotification(gameStarted) }
    }

    suspend fun notifyEnd(winner: Player<*>?) {
        val gameFinished = Notifications.GameFinished(winner?.name)
        players.forEach { it.sendNotification(gameFinished) }
    }

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
        get() = players[(whoseTurn.get() - 1) % 2]

    suspend fun run() = notifyBegin().also { whoseTurn.set(0) }
}
