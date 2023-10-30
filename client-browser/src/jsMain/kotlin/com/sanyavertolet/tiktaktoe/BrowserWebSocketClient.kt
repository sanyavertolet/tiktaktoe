package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.handlers.NotificationHandler
import com.sanyavertolet.tiktaktoe.messages.Requests
import io.ktor.client.engine.js.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BrowserWebSocketClient(
    private val userName: String,
    private val lobbyCode: String,
    private val notificationHandler: NotificationHandler,
    coroutineContext: CoroutineContext,
) : WebSocketClient(Js) {
    private val scope = CoroutineScope(coroutineContext)

    private fun startSession(
        requestBuilder: () -> Requests
    ) = scope.launch {
        startSessionAndRequest(SERVER_URL, notificationHandler::onNotificationReceived, requestBuilder)
    }

    fun startSession(options: Options?) = startSession {
        options?.let {
            Requests.CreateLobby(userName, lobbyCode, it.fieldSize, it.winCondition)
        } ?: Requests.JoinLobby(userName, lobbyCode)
    }

    fun sendTurnRequest(position: Position) = sendRequest(Requests.Turn(position, lobbyCode))

    fun sendGameStartRequest() = sendRequest(Requests.StartGame(lobbyCode))

    @Suppress("unused")
    companion object {
        const val URL = "kznet.ftp.sh:8080"
        const val DEBUG_URL = "localhost:8080"
    }
}