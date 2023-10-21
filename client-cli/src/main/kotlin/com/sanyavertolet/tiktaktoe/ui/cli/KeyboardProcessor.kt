package com.sanyavertolet.tiktaktoe.ui.cli

import com.sanyavertolet.tiktaktoe.utils.Direction
import kotlinx.coroutines.*
import org.jline.terminal.TerminalBuilder
import java.io.Reader
import kotlin.system.exitProcess

suspend fun processKeyboard(keyboardMapper: (CoroutineScope, Reader) -> Unit?) {
    withContext(Dispatchers.IO) {
        val terminal = TerminalBuilder.terminal()
        terminal.enterRawMode()
        val reader = terminal.reader()
        while (true) {
            keyboardMapper(this, reader) ?: exitProcess(0)
        }
    }
}

suspend fun processGameKeyboard(
    putMarker: suspend () -> Unit,
    updatePosition: (Direction) -> Unit,
) = processKeyboard { scope, reader ->
    when (reader.read()) {
        'q'.code -> null
        ' '.code -> scope.launch { coroutineScope { putMarker() } }.let { Unit }
        27 -> {
            when (reader.read()) {
                91 -> {
                    when (reader.read()) {
                        65 -> updatePosition(Direction.TOP)
                        66 -> updatePosition(Direction.BOTTOM)
                        67 -> updatePosition(Direction.RIGHT)
                        68 -> updatePosition(Direction.LEFT)
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
        else -> Unit
    }
}
