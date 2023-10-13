package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.game.Position
import io.ktor.websocket.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

abstract class User<O : Any>(
    val name: String,
    private val origin: O,
) {
    fun getOrigin() = origin

    abstract suspend fun waitForResponse(): Request.Turn

    abstract suspend fun sendMessage(message: String)

    abstract suspend fun disconnect(reasonMessage: String)
}

class WebSocketUser(
    name: String,
    webSocketSession: WebSocketSession,
) : User<WebSocketSession>(name, webSocketSession) {
    override suspend fun sendMessage(message: String) = getOrigin().send(message)

    override suspend fun waitForResponse(): Request.Turn {
        val response = getOrigin().incoming.receive() as? Frame.Text ?: throw IllegalStateException()
        return Json.decodeFromString(Request.Turn.serializer(), response.readText())
    }

    override suspend fun disconnect(reasonMessage: String) {
        getOrigin().close(CloseReason(CloseReason.Codes.NORMAL, reasonMessage))
    }
}

@Serializable
sealed class Notification {
    @Serializable
    data class GameStarted(val whoseTurnUserName: String) : Notification()

    @Serializable
    data class GameFinished(val whoWinsUserName: String?) : Notification()

    @Serializable
    data class Turn(val opponentPosition: Position?) : Notification()
}

@Serializable
sealed class Request {
    @Serializable
    data class CreateLobby(val userName: String) : Request()

    @Serializable
    data class JoinLobby(val userName: String, val lobbyCode: Int) : Request()

    @Serializable
    data class Turn(val position: Position) : Request()
}
