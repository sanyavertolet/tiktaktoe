package com.sanyavertolet.tiktaktoe.game

import com.sanyavertolet.tiktaktoe.multiplayer.Notification
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

    private fun turn(position: Position) {
        field[position] = currentTurnPlayer
    }

    private suspend fun notifyBegin() {
        val gameStarted = Notification.GameStarted(players.first().name)
        players.forEach { it.sendNotification(gameStarted) }
    }

    private suspend fun notifyEnd(winner: Player<*>?) {
        val gameFinished = Notification.GameFinished(winner?.name)
        players.forEach { it.sendNotification(gameFinished) }
    }

    private suspend fun askForTurn(position: Position?): Position {
        val turnNotification = Notification.Turn(position)
        currentTurnPlayer.sendNotification(turnNotification)
        return currentTurnPlayer.waitForTurn().position
    }

    private val currentTurnPlayer: Player<*>
        get() = players[whoseTurn.get()]

    suspend fun run() {
        notifyBegin()
        whoseTurn.set(0)
        var previousPosition: Position? = null
        while (true) {
            val position = askForTurn(previousPosition)
            turn(position).also { whoseTurn.incrementAndGet() }
            val (isFinished, winner) = whoWins()
            if (isFinished) {
                notifyEnd(players.find { it.type == winner })
                return
            }
            previousPosition = position
        }
    }
}
