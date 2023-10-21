package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.clikt.FieldType
import com.sanyavertolet.tiktaktoe.utils.markerPadding

@Composable
fun TicksAndTacks(field: FieldType, fieldSizeWithBorders: Int) {
    for ((pos, sym) in field) {
        Text(sym.ch, markerPadding(pos, fieldSizeWithBorders))
    }
}
