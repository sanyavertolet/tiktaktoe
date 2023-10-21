package com.sanyavertolet.tiktaktoe.utils

import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.modifier.Modifier
import com.sanyavertolet.tiktaktoe.game.Position

const val TOP_PADDING = 3

fun fieldPadding(y: Int) = Modifier.padding(vertical = TOP_PADDING + y)

fun fieldPadding(x: Int, y: Int, fieldSizeWithBorders: Int) = Modifier.padding(
    x,
    TOP_PADDING + y,
    fieldSizeWithBorders - x,
    TOP_PADDING + fieldSizeWithBorders - y,
)

fun markerPadding(pos: Position, fieldSizeWithBorders: Int) = Modifier.padding(
    pos.x.withBorders(),
    TOP_PADDING + pos.y.withBorders(),
    fieldSizeWithBorders - pos.x.withBorders(),
    fieldSizeWithBorders - pos.y.withBorders(),
)

fun Int.withBorders() = 2 * this + 1
