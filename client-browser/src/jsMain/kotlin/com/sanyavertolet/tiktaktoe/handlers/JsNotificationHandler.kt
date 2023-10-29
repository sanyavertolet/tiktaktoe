package com.sanyavertolet.tiktaktoe.handlers

import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.messages.Notifications

typealias PlayerJoinedCallback = (String?, Int, Int) -> Unit
typealias GameStartedCallback = (String) -> Unit
typealias GameFinishedCallback = (String?) -> Unit
typealias PlayerLeftCallback = () -> Unit
typealias TurnCallback = (Position, String) -> Unit
typealias ErrorCallback = (String, Boolean) -> Unit

class JsNotificationHandler(
    private val onPlayerJoined: PlayerJoinedCallback,
    private val onGameStarted: GameStartedCallback,
    private val onGameFinished: GameFinishedCallback,
    private val onPlayerLeft: PlayerLeftCallback,
    private val onTurn: TurnCallback,
    private val onError: ErrorCallback,
) : NotificationHandler {
    override fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) = with(playerJoined) {
        onPlayerJoined(anotherUserName, fieldSize, winCondition)
    }

    override fun onGameStarted(gameStarted: Notifications.GameStarted) = with(gameStarted) {
        onGameStarted(whoseTurnUserName)
    }

    override fun onGameFinished(gameFinished: Notifications.GameFinished) = with(gameFinished) {
        onGameFinished(whoWinsUserName)
    }

    override fun onPlayerLeft(playerLeft: Notifications.PlayerLeft) {
        onPlayerLeft()
    }

    override fun onTurn(turn: Notifications.Turn) = with(turn) {
        onTurn(position, userName)
    }

    override fun onError(error: Notifications.Error) = with(error) {
        onError(errorMessage, isCritical)
    }
}
