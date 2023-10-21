package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.Composable
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.utils.fieldPadding

private const val HORIZONTAL_DELIMITER = "|"
private const val VERTICAL_DELIMITER = "="

@Composable
fun Field(fieldSizeWithBorders: Int) {
    for (i in 0 until fieldSizeWithBorders) {
        if (i % 2 == 0) {
            for (j in 0 until fieldSizeWithBorders) {
                Text(VERTICAL_DELIMITER, fieldPadding(j, i, fieldSizeWithBorders))
            }
        } else {
            for (j in 0 until fieldSizeWithBorders) {
                if (j % 2 == 0) {
                    Text(HORIZONTAL_DELIMITER, fieldPadding(j, i, fieldSizeWithBorders))
                }
            }
        }
    }
}
