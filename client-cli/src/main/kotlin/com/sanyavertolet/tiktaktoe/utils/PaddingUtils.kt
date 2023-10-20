package com.sanyavertolet.tiktaktoe.utils

import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.modifier.Modifier
import com.sanyavertolet.tiktaktoe.World
import com.sanyavertolet.tiktaktoe.game.Position

fun fieldPadding(y: Int) = Modifier.padding(vertical = World.TOP_PADDING + y)

fun fieldPadding(x: Int, y: Int) = Modifier.padding(
    x,
    World.TOP_PADDING + y,
    World.fieldSizeWithBorders - x,
    World.fieldSizeWithBorders + World.TOP_PADDING - y,
)

fun fieldPadding(pos: Position) = fieldPadding(pos.x, pos.y)
