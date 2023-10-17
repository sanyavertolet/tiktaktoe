package com.sanyavertolet.tiktaktoe.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StartView() {
    var userName by remember { mutableStateOf("") }
    var lobbyCode by remember { mutableStateOf("") }

    TextField(
        userName,
        onValueChange = { userName = it },
        placeholder = { Text("User name...") },
    )

    TextField(
        lobbyCode,
        onValueChange = { lobbyCode = it },
        placeholder = { Text("Lobby code...") },
    )

    Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                TODO("Create lobby")
            },
        ) { Text("Create") }
    }

    Column(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                TODO("Join lobby")
            },
        ) { Text("Join") }
    }
}
