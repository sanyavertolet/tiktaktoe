package com.sanyavertolet.tiktaktoe.routing

import com.sanyavertolet.tiktaktoe.game.Options

data class GameRouteLoaderData(
    val userName: String,
    val lobbyCode: String,
    val options: Options?
)
