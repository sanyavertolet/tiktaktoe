package com.sanyavertolet.tiktaktoe.utils

import com.sanyavertolet.tiktaktoe.World
import com.sanyavertolet.tiktaktoe.game.Position

enum class Direction {
    TOP,
    LEFT,
    RIGHT,
    BOTTOM,
}

fun Position.move(direction: Direction) = when (direction) {
    Direction.LEFT -> -2 to 0
    Direction.TOP -> 0 to -2
    Direction.RIGHT -> 2 to 0
    Direction.BOTTOM -> 0 to 2
}.let { (deltaX, deltaY) ->
    Position(
        (x + deltaX).coerceIn(1, 2 * World.fieldSize - 1),
        (y + deltaY).coerceIn(1, 2 * World.fieldSize - 1),
    )
}
