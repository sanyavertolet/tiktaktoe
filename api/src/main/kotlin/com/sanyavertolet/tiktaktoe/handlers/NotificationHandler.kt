package com.sanyavertolet.tiktaktoe.handlers

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
