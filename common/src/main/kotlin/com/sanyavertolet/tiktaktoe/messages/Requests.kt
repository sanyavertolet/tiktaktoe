package com.sanyavertolet.tiktaktoe.messages

import com.sanyavertolet.tiktaktoe.game.Position
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Requests {
    @Serializable
    @SerialName("create")
    data class CreateLobby(
        val userName: String,
        val lobbyCode: String? = null,
        val settings: Pair<Int, Int> = 3 to 3,
    ) : Requests()

    @Serializable
    @SerialName("start")
    data class StartGame(val lobbyCode: String) : Requests()

    @Serializable
    @SerialName("join")
    data class JoinLobby(val userName: String, val lobbyCode: String) : Requests()

    @Serializable
    data class LeaveLobby(val userName: String, val lobbyCode: String) : Requests()

    @Serializable
    @SerialName("turn")
    data class Turn(val position: Position, val lobbyCode: String) : Requests()
}
