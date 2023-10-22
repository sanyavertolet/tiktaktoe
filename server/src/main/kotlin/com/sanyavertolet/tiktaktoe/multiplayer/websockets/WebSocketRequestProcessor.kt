package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import com.sanyavertolet.tiktaktoe.multiplayer.RequestProcessor
import io.ktor.websocket.*

class WebSocketRequestProcessor : RequestProcessor<WebSocketSession> {
    override suspend fun onCreateLobby(request: Requests.CreateLobby, session: WebSocketSession) {
        val user = WebSocketUser(request.userName, session)
        val lobby = request.lobbyCode?.let {
            Lobby(user, request.fieldSize, request.winCondition, it)
        } ?: Lobby(user, request.fieldSize, request.winCondition)
        Lobby.lobbies.add(lobby)
        lobby.notifyAll(
            Notifications.PlayerJoined(request.userName, request.fieldSize, request.winCondition),
        )
    }

    override suspend fun onJoinLobby(request: Requests.JoinLobby, session: WebSocketSession) {
        val user = WebSocketUser(request.userName, session)
        Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
            ?.also { it.connectUser(user) }
            ?.also { lobby ->
                lobby.notifyAll(
                    Notifications.PlayerJoined(request.userName, lobby.boardSize, lobby.rowWinCount),
                )
            }
    }

    override suspend fun onLeaveLobby(request: Requests.LeaveLobby, session: WebSocketSession) {
        val lobby = Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
        lobby?.disconnectUser(request.userName, session)
        lobby?.notifyAll(Notifications.PlayerLeft)
    }

    override suspend fun onStartGame(request: Requests.StartGame, session: WebSocketSession) {
        val lobby = Lobby.lobbies.find { it.host.origin == session && it.lobbyCode == request.lobbyCode }
            ?: error("Forbidden to create such lobby.")
        TikTakToeGame.games[lobby.lobbyCode] = lobby.createGame().also { it.run() }
    }

    override suspend fun onTurn(request: Requests.Turn, session: WebSocketSession) {
        val game = TikTakToeGame.games[request.lobbyCode] ?: error("No such game")
        request.position
            .let { pos ->
                pos.takeIf { game.currentTurnPlayer.origin == session } ?: error("Not your turn")
            }
            .let { game.turn(it) }
            ?.let {
                game.sendAll(Notifications.Turn(game.previousTurnPlayer.name, request.position))
            } ?: game.notifyEnd(game.previousTurnPlayer)
    }
}
