package com.sanyavertolet.tiktaktoe.game

import kotlinx.serialization.Serializable

@Serializable
data class Options(
    val fieldSize: Int,
    val winCondition: Int,
) {
    val fieldSizeWithBorders: Int
        get() = 2 * fieldSize + 1
}
