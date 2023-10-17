package com.sanyavertolet.tiktaktoe.multiplayer.websockets

import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

fun Routing.gameRoute() = webSocket("/game") {
    try {
        for (frame in incoming) {
            val message = frame as? Frame.Text ?: continue
            val messageText = message.readText()
            when (val request = Json.decodeFromString<Requests>(messageText)) {
                is Requests.CreateLobby -> {
                    val user = WebSocketUser(request.userName, this)
                    val lobby = request.lobbyCode?.let { Lobby(user, it) } ?: Lobby(user)
                    Lobby.lobbies.add(lobby)
                }

                is Requests.JoinLobby -> {
                    val user = WebSocketUser(request.userName, this)
                    Lobby.lobbies.find { it.lobbyCode == request.lobbyCode }?.connectUser(user)
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
                            val notification = Notifications.Turn(request.position)
                            game.currentTurnPlayer.sendNotification(notification)
                        } ?: game.notifyEnd(game.previousTurnPlayer)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

val games: ConcurrentHashMap<String, TikTakToeGame> = ConcurrentHashMap()
