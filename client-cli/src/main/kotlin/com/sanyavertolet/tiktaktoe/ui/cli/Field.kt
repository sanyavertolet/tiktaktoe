package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

private const val HORIZONTAL_DELIMITER = "|"
private const val VERTICAL_DELIMITER = "="

@Composable
fun Field(options: Options) {
    for (i in 0 until options.fieldSizeWithBorders) {
        if (i % 2 == 0) {
            for (j in 0 until options.fieldSizeWithBorders) {
                Text(VERTICAL_DELIMITER, fieldPadding(j, i, options.fieldSizeWithBorders))
            }
        } else {
            for (j in 0 until options.fieldSizeWithBorders) {
                if (j % 2 == 0) {
                    Text(HORIZONTAL_DELIMITER, fieldPadding(j, i, options.fieldSizeWithBorders))
                }
            }
        }
    }
}
