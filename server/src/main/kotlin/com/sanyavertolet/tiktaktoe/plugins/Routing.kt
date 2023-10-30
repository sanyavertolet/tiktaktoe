package com.sanyavertolet.tiktaktoe.plugins

import com.sanyavertolet.tiktaktoe.lobbies
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/lobbies") {
            println("HTTP: ${lobbies.size}")
            val lobbyDtos = lobbies.filter { it.users.size == 1 }.map { it.toDto() }
            println(lobbyDtos)
            call.respond(lobbyDtos)
        }
        staticResources("/", "public")
    }
}
