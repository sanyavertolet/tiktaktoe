package com.sanyavertolet.tiktaktoe

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import com.jakewharton.mosaic.layout.padding
import com.jakewharton.mosaic.modifier.Modifier
import com.jakewharton.mosaic.runMosaic
import com.jakewharton.mosaic.ui.Column
import com.jakewharton.mosaic.ui.Text
import com.sanyavertolet.tiktaktoe.game.Position
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jline.terminal.TerminalBuilder

private const val width = 20
private const val height = 10

private const val fieldSize = 5

enum class Symbol(val ch: String) {
    TICK("0"),
    CROSS("X"),
    NONE(" "),
    CURSOR("^"),
}

suspend fun main() = runMosaic {
    var currentX by mutableStateOf(0)
    var currentY by mutableStateOf(0)
    val symbol by mutableStateOf(Symbol.CROSS)

    val field: SnapshotStateMap<Position, Symbol> = mutableStateMapOf()

    setContent {
        Column {
            Text("Use arrow keys to move the face. Press “q” to exit.")
            Text("Position: $currentX, $currentY   World: $width, $height")
            Text("")

            for ((pos, sym) in field) {
                Text(sym.ch, modifier = Modifier.padding(pos.x, pos.y, fieldSize - pos.x, fieldSize - pos.y))
            }

            Text("$", modifier = Modifier.padding(currentX, currentY, fieldSize - currentX, fieldSize - currentY))
        }
    }

    val putMarker: (x: Int, y: Int) -> Unit = { x, y ->
        field[Position(x, y)] = symbol
    }

    withContext(Dispatchers.IO) {
        val terminal = TerminalBuilder.terminal()
        terminal.enterRawMode()
        val reader = terminal.reader()

        while (true) {
            when (reader.read()) {
                'q'.code -> break
                13 -> putMarker(currentX, currentY)
                27 -> {
                    when (reader.read()) {
                        91 -> {
                            when (reader.read()) {
                                65 -> currentY = (currentY - 1).coerceAtLeast(0)
                                66 -> currentY = (currentY + 1).coerceAtMost(height)
                                67 -> currentX = (currentX + 1).coerceAtMost(width)
                                68 -> currentX = (currentX - 1).coerceAtLeast(0)
                            }
                        }
                    }
                }
            }
        }
    }
}
