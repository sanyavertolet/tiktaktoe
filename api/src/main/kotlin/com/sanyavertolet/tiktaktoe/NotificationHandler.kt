package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.messages.Notifications

interface NotificationHandler {
    fun onNotificationReceived(notification: Notifications) {
        when (notification) {
            is Notifications.Turn -> onTurn(notification)
            is Notifications.PlayerJoined -> onPlayerJoined(notification)
            is Notifications.GameStarted -> onGameStarted(notification)
            is Notifications.PlayerLeft -> onPlayerLeft(notification)
            is Notifications.GameFinished -> onGameFinished(notification)
            is Notifications.Error -> onError(notification)
        }
    }

    fun onPlayerJoined(playerJoined: Notifications.PlayerJoined)

    fun onGameStarted(gameStarted: Notifications.GameStarted)

    fun onGameFinished(gameFinished: Notifications.GameFinished)

    fun onPlayerLeft(playerLeft: Notifications.PlayerLeft)

    fun onTurn(turn: Notifications.Turn)

    fun onError(error: Notifications.Error)
}

abstract class CliNotificationHandler(
    private val errorHandler: ErrorHandler,
    private val exit: (Int) -> Unit,
) : NotificationHandler {
    override fun onGameFinished(gameFinished: Notifications.GameFinished) {
        println("${gameFinished.whoWinsUserName} wins!")
        exit(0)
    }

    override fun onPlayerLeft(playerLeft: Notifications.PlayerLeft) {
        println("User disconnected, you are the winner!")
        exit(0)
    }

    override fun onError(error: Notifications.Error) = errorHandler.onError(error)
}
