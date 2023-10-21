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
import com.jakewharton.mosaic.runMosaic
import com.sanyavertolet.tiktaktoe.WebSocketClient
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests
import com.sanyavertolet.tiktaktoe.ui.cli.*
import com.sanyavertolet.tiktaktoe.utils.move
import com.sanyavertolet.tiktaktoe.utils.withBorders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

typealias FieldType = SnapshotStateMap<Position, Marker>

abstract class GameCommand(help: String) : CliktCommand(help = help) {
    protected open val url: String by option("--url", "-u").default("localhost").help { "Server url" }
    open val userName: String by argument("username").help { "User name" }
    open val lobbyCode: String by option("--lobby", "-l").required().help { "Lobby code" }

    abstract val fieldSize: Int
    abstract val winCondition: Int
    private val gameScope = CoroutineScope(Dispatchers.Default)
    private val webSocketClient = WebSocketClient()

    private var isMyTurn: Boolean = false
    private lateinit var myMarker: Marker
    private val opponentMarker: Marker
        get() = when (myMarker) {
            Marker.TAC -> Marker.TIC
            Marker.TIC -> Marker.TAC
        }

    protected abstract fun getInitRequest(): Requests

    private suspend fun initializeConnection() = webSocketClient.startSessionAndRequest(url, ::onNotificationReceived, ::getInitRequest)

    override fun run() {
        processConnection()
    }

    private fun processConnection() = runBlocking {
        runCatching { initializeConnection() }.onFailure { it.printStackTrace().also { exitProcess(1) } }
    }

    private val field: FieldType = mutableStateMapOf()

    protected fun processGameUi() = gameScope.launch { startGame() }

    private fun onNotificationReceived(notification: Notifications) {
        when (notification) {
            is Notifications.Turn -> onTurn(notification)
            is Notifications.PlayerJoined -> onPlayerJoined(notification)
            is Notifications.GameStarted -> onGameStarted(notification)
            is Notifications.PlayerLeft -> onPlayerLeft(notification)
            is Notifications.GameFinished -> onGameFinished(notification)
        }
    }

    protected abstract fun getLobbyMessage(): String

    protected open fun onPlayerJoined(playerJoined: Notifications.PlayerJoined) {
        if (playerJoined.userName == userName) {
            println(getLobbyMessage())
        } else {
            println("To start a game, press \"g\" button...")
            webSocketClient.sendRequest(Requests.StartGame(lobbyCode))
        }
    }

    protected open fun onGameStarted(gameStarted: Notifications.GameStarted) {
        myMarker = if (gameStarted.whoseTurnUserName == userName) Marker.TIC else Marker.TAC
        isMyTurn = myMarker == Marker.TIC
        processGameUi()
    }

    protected open fun onGameFinished(gameFinished: Notifications.GameFinished) {
        println("${gameFinished.whoWinsUserName} wins!")
        exitProcess(0)
    }

    protected open fun onPlayerLeft(playerLeft: Notifications.PlayerLeft) {
        println("User disconnected, you are the winner!")
        exitProcess(0)
    }

    protected open fun onTurn(turn: Notifications.Turn) {
        if (turn.userName != userName) {
            field[turn.position] = opponentMarker
            isMyTurn = true
        }
    }

    private suspend fun startGame() = runMosaic {
        field.clear()
        var currentPos by mutableStateOf(Position(0, 0))

        setContent {
            Header(currentPos, fieldSize, winCondition)
            Field(fieldSize.withBorders())
            TicksAndTacks(field, fieldSize.withBorders())
            Pointer(currentPos, fieldSize.withBorders())
        }

        val putMarker: suspend () -> Unit = {
            if (isMyTurn) {
                field[currentPos] = myMarker
                sendTurnRequest(currentPos)
                isMyTurn = !isMyTurn
            }
        }

        processGameKeyboard(putMarker) { currentPos = currentPos.move(it, fieldSize) }
    }

    protected open suspend fun sendTurnRequest(position: Position) {
        webSocketClient.sendRequest(Requests.Turn(position, lobbyCode))
    }
}
