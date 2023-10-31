package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.exceptions.ErrorHandler
import com.sanyavertolet.tiktaktoe.exceptions.GameException
import com.sanyavertolet.tiktaktoe.exceptions.NotifyingWebSocketErrorHandler
import com.sanyavertolet.tiktaktoe.game.GarbageCollector
import com.sanyavertolet.tiktaktoe.games
import com.sanyavertolet.tiktaktoe.lobbies
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import com.sanyavertolet.tiktaktoe.multiplayer.RequestProcessor
import io.ktor.websocket.*

class WebSocketRequestProcessor(
    private val errorHandler: ErrorHandler<DefaultWebSocketSession> = NotifyingWebSocketErrorHandler(),
) : RequestProcessor<DefaultWebSocketSession> {
    override suspend fun onError(
        exception: GameException,
        session: DefaultWebSocketSession,
    ) = errorHandler.onError(exception, session)

    override suspend fun onCreateLobby(request: Requests.CreateLobby, session: DefaultWebSocketSession) {
        val user = WebSocketUser(request.userName, session)
        val lobby = request.lobbyCode?.let {
            Lobby(user, request.fieldSize, request.winCondition, it)
        } ?: Lobby(user, request.fieldSize, request.winCondition)
        lobbies.add(lobby)
        user.sendNotification(
            Notifications.PlayerJoined(null, request.fieldSize, request.winCondition),
        )
    }

    override suspend fun onJoinLobby(request: Requests.JoinLobby, session: DefaultWebSocketSession) = lobbies.find {
        it.lobbyCode == request.lobbyCode
    }
        .orGameException("Lobby [${request.lobbyCode}] was not found.", true)
        .takeIf { it.host.name != request.userName }
        .orGameException("Nickname ${request.userName} is busy.", true)
        .let { lobby ->
            val user = WebSocketUser(request.userName, session)
            lobby.connectUser(user)
            lobby to user
        }
        .let { (lobby, user) ->
            lobby.host.sendNotification(
                Notifications.PlayerJoined(request.userName, lobby.boardSize, lobby.rowWinCount),
            )
            user.sendNotification(
                Notifications.PlayerJoined(lobby.host.name, lobby.boardSize, lobby.rowWinCount),
            )
        }

    override suspend fun onLeaveLobby(request: Requests.LeaveLobby, session: DefaultWebSocketSession) {
        val lobby = lobbies.find { it.lobbyCode == request.lobbyCode }
            .orGameException("Could not find lobby with code [${request.lobbyCode}].", true)
        lobby.disconnectUser(request.userName, session)
        lobby.notifyAll(Notifications.PlayerLeft)
        lobbies.remove(lobby)
        games.remove(request.lobbyCode)
    }

    override suspend fun onStartGame(request: Requests.StartGame, session: DefaultWebSocketSession) {
        lobbies.find { it.host.origin == session && it.lobbyCode == request.lobbyCode }
            ?.also { lobby -> games[lobby.lobbyCode] = lobby.createGame().also { game -> game.run() } }
            .orGameException("Forbidden to create such lobby.", true)
    }

    override suspend fun onTurn(request: Requests.Turn, session: DefaultWebSocketSession) {
        games[request.lobbyCode]?.let { game ->
            request.position
                .let { pos ->
                    pos.takeIf { game.currentTurnPlayer.origin == session } ?: throw GameException(
                        "Not your turn",
                        false
                    )
                }
                .let { game.turn(it) }
                ?.let { game.notifyAll(Notifications.Turn(game.previousTurnPlayer.name, request.position)) }
                ?: GarbageCollector.cleanUp(request.lobbyCode)
        } ?: throw GameException("Lobby with code [${request.lobbyCode}] was not found.", true)
    }

    private fun Lobby<DefaultWebSocketSession>?.orGameException(
        message: String,
        isCritical: Boolean,
    ) = this ?: throw GameException(message, isCritical)
}
