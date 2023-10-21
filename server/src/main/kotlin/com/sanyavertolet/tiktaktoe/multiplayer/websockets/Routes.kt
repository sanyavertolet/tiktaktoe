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
import java.util.concurrent.ConcurrentHashMap

private val webSocketLogger = LoggerFactory.getLogger("webSocketLogger")

fun Routing.gameRoute() = webSocket("/game") {
    try {
        for (frame in incoming) {
            val message = frame as? Frame.Text ?: continue
            val messageText = message.readText()
            webSocketLogger.trace("Receiving $messageText")
            when (val request = Json.decodeFromString<Requests>(messageText)) {
                is Requests.CreateLobby -> {
                    val user = WebSocketUser(request.userName, this)
                    val lobby = request.lobbyCode?.let {
                        Lobby(user, request.fieldSize, request.winCondition, it)
                    } ?: Lobby(user, request.fieldSize, request.winCondition)
                    Lobby.lobbies.add(lobby)
                    lobby.notifyAll(
                        Notifications.PlayerJoined(request.userName, request.fieldSize, request.winCondition),
                    )
                }

                is Requests.JoinLobby -> {
                    val user = WebSocketUser(request.userName, this)
                    Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
                        ?.also { it.connectUser(user) }
                        ?.also { lobby ->
                            lobby.notifyAll(
                                Notifications.PlayerJoined(request.userName, lobby.boardSize, lobby.rowWinCount),
                            )
                        }
                }

                is Requests.LeaveLobby -> {
                    val lobby = Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }
                    lobby?.disconnectUser(request.userName, this)
                    lobby?.notifyAll(Notifications.PlayerLeft)
                }

                is Requests.StartGame -> {
                    val lobby = Lobby.lobbies.find { it.host.origin == this } ?: error("Forbidden to create such a lobby.")
                    games[lobby.lobbyCode] = lobby.createGame().also { it.run() }
                }

                is Requests.Turn -> {
                    val game = games[request.lobbyCode] ?: continue
                    request.position
                        .let { pos ->
                            pos.takeIf { game.currentTurnPlayer.origin == this } ?: error("Not your turn")
                        }
                        .let { game.turn(it) }
                        ?.let {
                            game.sendAll(Notifications.Turn(game.previousTurnPlayer.name, request.position))
                        } ?: game.notifyEnd(game.previousTurnPlayer)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

val games: ConcurrentHashMap<String, TikTakToeGame> = ConcurrentHashMap()
