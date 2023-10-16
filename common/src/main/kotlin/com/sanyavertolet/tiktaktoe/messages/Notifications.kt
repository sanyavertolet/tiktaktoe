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
    data class Turn(val opponentPosition: Position?) : Notifications()
}
