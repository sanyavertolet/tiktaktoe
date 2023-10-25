package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.markerPadding

@Composable
fun Pointer(currentPos: Position, options: Options, sym: String = "^") {
    Text(sym, modifier = markerPadding(currentPos, options.fieldSizeWithBorders))
}
