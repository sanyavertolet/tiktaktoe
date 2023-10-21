package com.sanyavertolet.tiktaktoe.clikt

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.sanyavertolet.tiktaktoe.messages.Requests

class Create : GameCommand("Create lobby") {
    override val fieldSize: Int by option("--field_size", "-s").int().default(3).help { "Field size" }
    override val winCondition: Int by option("--win_condition", "-c").int().default(3).help { "Win condition" }
    override fun getInitRequest(): Requests = Requests.CreateLobby(userName, lobbyCode, fieldSize, winCondition)
    override fun getLobbyMessage(): String = "Waiting for another user..."
}
