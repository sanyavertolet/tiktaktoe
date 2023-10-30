package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.game.GarbageCollector
import com.sanyavertolet.tiktaktoe.game.TikTakToeGame
import com.sanyavertolet.tiktaktoe.multiplayer.Lobby
import com.sanyavertolet.tiktaktoe.plugins.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.util.collections.*
import io.ktor.websocket.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

typealias LobbySet = MutableSet<Lobby<DefaultWebSocketSession>>
typealias GameMap = ConcurrentHashMap<String, TikTakToeGame>

val lobbies: LobbySet = ConcurrentSet()
val games: GameMap = ConcurrentHashMap()

fun Application.module() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSockets()
    configureRouting()
    GarbageCollector().run(1.seconds)
}
