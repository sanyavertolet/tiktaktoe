package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Color
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.clikt.FieldType
import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.utils.markerPadding

@Composable
fun TicksAndTacks(field: FieldType, options: Options) {
    for ((pos, sym) in field) {
        val color = when (sym) {
            MarkerType.TIC -> Color.BrightBlue
            MarkerType.TAC -> Color.Red
        }
        Text(sym.ch, markerPadding(pos, options.fieldSizeWithBorders), color = color)
    }
}
