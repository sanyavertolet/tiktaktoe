package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.messages.Requests
import com.sanyavertolet.tiktaktoe.plugins.configureRouting
import com.sanyavertolet.tiktaktoe.plugins.configureSockets
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.server.testing.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun httpRouteTest() = testApplication {
        application { configureRouting() }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun wsRouteTest() = testApplication {
        application { configureSockets() }

        val scope = CoroutineScope(Dispatchers.Default)
        val hostNotification: Deferred<Deferred<Notifications>> = scope.async {
            val notification: CompletableDeferred<Notifications> = CompletableDeferred()
            createClient {
                install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(Json) }
            }
                .ws("/game") {
                    val createRequest: Requests = Requests.CreateLobby("user1", 1)
                    sendSerialized(createRequest)
                    val startRequest: Requests = Requests.StartGame(1)
                    sendSerialized(startRequest)
                    val text = (incoming.receive() as Frame.Text).readText()
                    notification.complete(text.let { Json.decodeFromString(it) })
                }
            notification
        }

        val playerNotification: Deferred<Deferred<Notifications>> = scope.async {
            val notification: CompletableDeferred<Notifications> = CompletableDeferred()
            createClient {
                install(WebSockets) {
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                }
            }
                .ws("/game") {
                    val request: Requests = Requests.JoinLobby("user2", 1)
                    sendSerialized(request)
                    val text = (incoming.receive() as Frame.Text).readText()
                    notification.complete(text.let { Json.decodeFromString(it) })
                }
            notification
        }

        assert(hostNotification.await().await() is Notifications.GameStarted)
        assert(playerNotification.await().await() is Notifications.GameStarted)
        assertEquals(
            (hostNotification.await().await() as Notifications.GameStarted).whoseTurnUserName,
            (playerNotification.await().await() as Notifications.GameStarted).whoseTurnUserName,
        )
    }
}
