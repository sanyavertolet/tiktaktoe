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
    2 * pos.x + 1,
    TOP_PADDING + 2 * pos.y + 1,
    fieldSizeWithBorders - (2 * pos.x + 1),
    fieldSizeWithBorders - (2 * pos.y + 1),
)
