package com.sanyavertolet.tiktaktoe.clikt

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import kotlin.properties.Delegates

class Join : GameCommand("Join lobby") {
    override var fieldSize by Delegates.notNull<Int>()
    override var winCondition by Delegates.notNull<Int>()
    override fun getInitRequest(): Requests = Requests.JoinLobby(userName, lobbyCode)
    override fun getLobbyMessage(): String = "Waiting for host to start game..."

    override fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) {
        super.onPlayerJoined(playerJoined)
        fieldSize = playerJoined.fieldSize
        winCondition = playerJoined.winCondition
    }
}
