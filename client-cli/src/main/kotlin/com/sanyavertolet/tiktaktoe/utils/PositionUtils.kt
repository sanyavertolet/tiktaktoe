package com.sanyavertolet.tiktaktoe.utils

import com.sanyavertolet.tiktaktoe.game.Position

enum class Direction {
    TOP,
    LEFT,
    RIGHT,
    BOTTOM,
}

fun Position.move(direction: Direction, fieldSize: Int) = when (direction) {
    Direction.LEFT -> -1 to 0
    Direction.TOP -> 0 to -1
    Direction.RIGHT -> 1 to 0
    Direction.BOTTOM -> 0 to 1
}.let { (deltaX, deltaY) ->
    Position(
        (x + deltaX).coerceIn(0, fieldSize - 1),
        (y + deltaY).coerceIn(0, fieldSize - 1),
    )
}
