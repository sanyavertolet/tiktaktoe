package com.sanyavertolet.tiktaktoe.clikt

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.help
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.sanyavertolet.tiktaktoe.Client
import com.sanyavertolet.tiktaktoe.WebSocketClient
import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.handlers.CliNotificationHandler
import com.sanyavertolet.tiktaktoe.handlers.ErrorHandler
import com.sanyavertolet.tiktaktoe.handlers.NotifyingErrorHandler
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.ui.cli.*
import io.ktor.client.engine.cio.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

typealias FieldType = SnapshotStateMap<Position, MarkerType>

abstract class GameCommand(help: String) : CliktCommand(help = help) {
    protected open val url: String by option("--url", "-u").default("kznet.ftp.sh:8080").help {
        "Server hostname and port in format \"url:port\""
    }
    open val userName: String by argument("username").help { "User name" }
    open val lobbyCode: String by option("--lobby", "-l").required().help {
        "Desired lobby code, which will be used to connect to lobby"
    }

    abstract val options: Options
    private val gameScope = CoroutineScope(Dispatchers.Default)
    private val client: Client = WebSocketClient(CIO)
    private val errorHandler: ErrorHandler = NotifyingErrorHandler(::exit)

    protected open fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) {
        if (playerJoined.anotherUserName != null) {
            println(getLobbyMessage())
        } else {
            println("To start a game, press \"g\" button...")
            client.sendRequest(Requests.StartGame(lobbyCode))
        }
    }

    private val notificationHandler = object : CliNotificationHandler(errorHandler, ::exit) {
        override fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) = this@GameCommand.onPlayerJoined(playerJoined)

        override fun onGameStarted(gameStarted: Notifications.GameStarted) {
            myMarker = if (gameStarted.whoseTurnUserName == userName) MarkerType.TIC else MarkerType.TAC
            isMyTurn = myMarker == MarkerType.TIC
            processGameUi()
        }

        override fun onTurn(turn: Notifications.Turn) {
            if (turn.userName != userName) {
                field[turn.position] = opponentMarker
                isMyTurn = true
            }
        }
    }

    private var isMyTurn: Boolean = false
    private lateinit var myMarker: MarkerType
    private val opponentMarker: MarkerType
        get() = when (myMarker) {
            MarkerType.TAC -> MarkerType.TIC
            MarkerType.TIC -> MarkerType.TAC
        }

    protected abstract fun getInitRequest(): Requests

    private suspend fun initializeConnection() = client.startSessionAndRequest(
        url,
        notificationHandler::onNotificationReceived,
        ::getInitRequest,
    )

    override fun run() {
        processConnection()
    }

    private fun processConnection() = runBlocking {
        runCatching { initializeConnection() }.onFailure(errorHandler::onError)
    }

    private val field: FieldType = mutableStateMapOf()

    protected fun processGameUi() {
        gameScope.launch {
            startGame(field, options, ::exit) {
                if (isMyTurn && field[it] == null) {
                    field[it] = myMarker
                    sendTurnRequest(it)
                    isMyTurn = !isMyTurn
                }
            }
        }
            .invokeOnCompletion { errorOrNull -> errorOrNull?.let { error -> errorHandler.onError(error) } }
    }

    protected abstract fun getLobbyMessage(): String

    protected open suspend fun sendTurnRequest(position: Position) {
        client.sendRequest(Requests.Turn(position, lobbyCode))
    }

    private fun exit(status: Int) {
        try {
            client.sendRequest(Requests.LeaveLobby(userName, lobbyCode))
        } finally {
            exitProcess(status)
        }
    }
}
