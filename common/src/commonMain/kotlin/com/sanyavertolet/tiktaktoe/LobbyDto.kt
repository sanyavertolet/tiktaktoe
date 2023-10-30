package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.game.Options
import kotlinx.serialization.Serializable

@Serializable
data class LobbyDto(
    val hostName: String,
    val options: Options,
    val lobbyCode: String,
)
