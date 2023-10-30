package com.sanyavertolet.tiktaktoe.routing

import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.views.errorView
import com.sanyavertolet.tiktaktoe.views.game.gameView
import com.sanyavertolet.tiktaktoe.views.welcome.welcomeView
import io.ktor.http.*
import js.core.jso
import react.create
import react.router.dom.createHashRouter

fun createRouter() = createHashRouter(
    arrayOf(
        jso {
            id = "welcome-route"
            path = "/"
            element = welcomeView.create()
        },
        jso {
            id = "game-route"
            path = ":userName/:lobbyCode"
            element = gameView.create()
            loader = { args ->
                val searchParams = Url(args.request.url).parameters
                val fieldSize = searchParams["s"]?.toIntOrNull()
                val winCondition = searchParams["c"]?.toIntOrNull()
                val options = if (fieldSize != null && winCondition != null) Options(fieldSize, winCondition) else null
                GameRouteLoaderData(
                    args.params["userName"]!!,
                    args.params["lobbyCode"]!!,
                    options,
                )
            }
        },
        jso {
            id = "error-route"
            path = "*"
            element = errorView.create()
        },
    )
)
