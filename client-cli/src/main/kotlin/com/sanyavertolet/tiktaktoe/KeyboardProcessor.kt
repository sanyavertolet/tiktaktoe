package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.utils.Direction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jline.terminal.TerminalBuilder

suspend fun processKeyboard(putMarker: () -> Unit, updatePosition: (Direction) -> Unit) {
    withContext(Dispatchers.IO) {
        val terminal = TerminalBuilder.terminal()
        terminal.enterRawMode()
        val reader = terminal.reader()

        while (true) {
            when (reader.read()) {
                'q'.code -> break
                ' '.code -> putMarker()
                27 -> {
                    when (reader.read()) {
                        91 -> {
                            when (reader.read()) {
                                65 -> updatePosition(Direction.TOP)
                                66 -> updatePosition(Direction.BOTTOM)
                                67 -> updatePosition(Direction.RIGHT)
                                68 -> updatePosition(Direction.LEFT)
                            }
                        }
                    }
                }
            }
        }
    }
}
