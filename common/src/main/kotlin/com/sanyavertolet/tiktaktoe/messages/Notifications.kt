package com.sanyavertolet.tiktaktoe.messages

import com.sanyavertolet.tiktaktoe.game.Position
import kotlinx.serialization.Serializable

@Serializable
sealed class Notifications {
    @Serializable
    data class GameStarted(val whoseTurnUserName: String) : Notifications()

    @Serializable
    data class GameFinished(val whoWinsUserName: String?) : Notifications()

    @Serializable
    data class PlayerJoined(val userName: String, val fieldSize: Int, val winCondition: Int) : Notifications()

    @Serializable
    data object PlayerLeft : Notifications()

    @Serializable
    data class Turn(val userName: String, val position: Position) : Notifications()

    @Serializable
    data class Error(val errorMessage: String, val stackTrace: String) : Notifications()
}
