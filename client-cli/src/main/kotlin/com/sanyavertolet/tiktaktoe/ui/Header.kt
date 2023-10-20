package com.sanyavertolet.tiktaktoe.ui

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

@Composable
fun Header(currentPos: Position, fieldSize: Int) {
    Text("Use arrow keys to move the face. Press “q” to exit.", fieldPadding(-3))
    Text("Position: $currentPos  World: $fieldSize, $fieldSize", fieldPadding(-2))
}
