package com.sanyavertolet.tiktaktoe.game

import kotlinx.serialization.Serializable

@Serializable
data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "(${x + 1}, ${y + 1})"
}
