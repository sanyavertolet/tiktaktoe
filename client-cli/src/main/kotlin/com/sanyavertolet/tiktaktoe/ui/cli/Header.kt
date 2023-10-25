package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

@Composable
fun Header(currentPos: Position, options: Options) {
    Text("Use arrow keys to move the face, space to put an icon. Press “q” to exit.", fieldPadding(-3))
    Text("Position: $currentPos  World: ${options.fieldSize}  Win Condition: ${options.winCondition}", fieldPadding(-2))
}
