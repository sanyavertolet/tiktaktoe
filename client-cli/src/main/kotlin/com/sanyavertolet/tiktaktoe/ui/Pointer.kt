package com.sanyavertolet.tiktaktoe.ui

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

@Composable
fun Pointer(currentPos: Position, sym: String = "^") {
    Text(sym, modifier = fieldPadding(currentPos))
}
