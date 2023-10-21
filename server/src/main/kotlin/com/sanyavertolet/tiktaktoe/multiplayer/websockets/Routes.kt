package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

private val webSocketLogger = LoggerFactory.getLogger("webSocketLogger")

fun Routing.gameRoute() = webSocket("/game") {
    try {
        for (frame in incoming) {
            val message = frame as? Frame.Text ?: continue
            val messageText = message.readText()
            webSocketLogger.trace("Receiving $messageText")
            when (val request = Json.decodeFromString<Requests>(messageText)) {
                is Requests.CreateLobby -> onCreateLobby(request, this)
                is Requests.JoinLobby -> onJoinLobby(request, this)
                is Requests.LeaveLobby -> onLeaveLobby(request, this)
                is Requests.StartGame -> onStartGame(request, this)
                is Requests.Turn -> onTurn(request, this)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private suspend fun onCreateLobby(request: Requests.CreateLobby, webSocketSession: WebSocketSession) {
    val user = WebSocketUser(request.userName, webSocketSession)
    val lobby = request.lobbyCode?.let {
        Lobby(user, request.fieldSize, request.winCondition, it)
    } ?: Lobby(user, request.fieldSize, request.winCondition)
    Lobby.lobbies.add(lobby)
    lobby.notifyAll(
        Notifications.PlayerJoined(request.userName, request.fieldSize, request.winCondition),
    )
}

private suspend fun onJoinLobby(request: Requests.JoinLobby, webSocketSession: WebSocketSession) {
    val user = WebSocketUser(request.userName, webSocketSession)
    Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
        ?.also { it.connectUser(user) }
        ?.also { lobby ->
            lobby.notifyAll(
                Notifications.PlayerJoined(request.userName, lobby.boardSize, lobby.rowWinCount),
            )
        }
}

private suspend fun onLeaveLobby(request: Requests.LeaveLobby, webSocketSession: WebSocketSession) {
    val lobby = Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
    lobby?.disconnectUser(request.userName, webSocketSession)
    lobby?.notifyAll(Notifications.PlayerLeft)
}

private suspend fun onStartGame(request: Requests.StartGame, webSocketSession: WebSocketSession) {
    val lobby = Lobby.lobbies.find { it.host.origin == webSocketSession && it.lobbyCode == request.lobbyCode }
        ?: error("Forbidden to create such lobby.")
    TikTakToeGame.games[lobby.lobbyCode] = lobby.createGame().also { it.run() }
}

private suspend fun onTurn(request: Requests.Turn, webSocketSession: WebSocketSession) {
    val game = TikTakToeGame.games[request.lobbyCode] ?: error("No such game")
    request.position
        .let { pos ->
            pos.takeIf { game.currentTurnPlayer.origin == webSocketSession } ?: error("Not your turn")
        }
        .let { game.turn(it) }
        ?.let {
            game.sendAll(Notifications.Turn(game.previousTurnPlayer.name, request.position))
        } ?: game.notifyEnd(game.previousTurnPlayer)
}
