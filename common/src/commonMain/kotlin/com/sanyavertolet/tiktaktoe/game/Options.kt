package com.sanyavertolet.tiktaktoe.game

import kotlinx.serialization.Serializable

@Serializable
data class Options(
    val fieldSize: Int,
    val winCondition: Int,
) {
    val fieldSizeWithBorders: Int
        get() = 2 * fieldSize + 1

    companion object {
        const val MIN_FIELD_SIZE = 3
        const val MIN_WIN_CONDITION = 2
        const val MAX_FIELD_SIZE = 10
        const val MAX_WIN_CONDITION = 10
        val default = Options(3, 3)
    }
}
