package com.sanyavertolet.tiktaktoe.ui

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.World
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

private const val HORIZONTAL_DELIMITER = "|"
private const val VERTICAL_DELIMITER = "="

@Composable
fun Field() {
    for (i in 0..2 * World.fieldSize) {
        if (i % 2 == 0) {
            for (j in 0..2 * World.fieldSize) {
                Text(VERTICAL_DELIMITER, fieldPadding(j, i))
            }
        } else {
            for (j in 0..2 * World.fieldSize) {
                if (j % 2 == 0) {
                    Text(HORIZONTAL_DELIMITER, fieldPadding(j, i))
                }
            }
        }
    }
}
