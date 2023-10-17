package com.sanyavertolet.tiktaktoe

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.sanyavertolet.tiktaktoe.views.StartView

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "TikTakToe",
        state = rememberWindowState(width = 500.dp, height = 500.dp),
    ) {
        MaterialTheme {
            StartView()
        }
    }
}
