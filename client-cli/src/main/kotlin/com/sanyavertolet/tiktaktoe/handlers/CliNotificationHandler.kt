package com.sanyavertolet.tiktaktoe.handlers

import com.sanyavertolet.tiktaktoe.messages.Notifications

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
