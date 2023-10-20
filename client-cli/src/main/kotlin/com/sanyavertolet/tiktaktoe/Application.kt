package com.sanyavertolet.tiktaktoe

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.jakewharton.mosaic.runMosaic
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.ui.Field
import com.sanyavertolet.tiktaktoe.ui.Header
import com.sanyavertolet.tiktaktoe.ui.Pointer
import com.sanyavertolet.tiktaktoe.utils.fieldPadding
import com.sanyavertolet.tiktaktoe.utils.move

suspend fun main() = runMosaic {
    var currentPos by mutableStateOf(Position(1, 1))
    val symbol by mutableStateOf(Symbol.CROSS)

    val field: SnapshotStateMap<Position, Symbol> = mutableStateMapOf()

    setContent {
        Header(currentPos, World.fieldSize)
        Field()

        for ((pos, sym) in field) {
            Text(sym.ch, fieldPadding(pos))
        }

        Pointer(currentPos)
    }

    processKeyboard({ field[currentPos] = symbol }) { currentPos = currentPos.move(it) }
}
