package com.sanyavertolet.tiktaktoe.clikt

import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests

class Join : GameCommand("Join lobby") {
    override lateinit var options: Options
    override fun getInitRequest(): Requests = Requests.JoinLobby(userName, lobbyCode)
    override fun getLobbyMessage(): String = "Waiting for host to start game..."

    override fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) {
        super.onPlayerJoined(playerJoined)
        options = Options(playerJoined.fieldSize, playerJoined.winCondition)
    }
}
