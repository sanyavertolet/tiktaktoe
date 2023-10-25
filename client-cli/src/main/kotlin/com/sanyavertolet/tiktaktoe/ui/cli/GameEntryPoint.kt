package com.sanyavertolet.tiktaktoe.ui.cli

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jakewharton.mosaic.runMosaic
import com.sanyavertolet.tiktaktoe.clikt.FieldType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.move

suspend fun startGame(
    field: FieldType,
    options: Options,
    exit: (Int) -> Unit,
    putMarker: suspend (Position) -> Unit,
) = runMosaic {
    field.clear()
    var currentPos by mutableStateOf(Position(0, 0))

    setContent {
        Header(currentPos, options)
        Field(options)
        TicksAndTacks(field, options)
        Pointer(currentPos, options)
    }

    processGameKeyboard(exit, { putMarker(currentPos) }) { currentPos = currentPos.move(it, options.fieldSize) }
}
