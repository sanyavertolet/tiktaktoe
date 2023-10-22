package com.sanyavertolet.tiktaktoe.multiplayer

import com.sanyavertolet.tiktaktoe.messages.Requests

interface RequestProcessor<S> {
    suspend fun onRequest(request: Requests, session: S) = when (request) {
        is Requests.CreateLobby -> onCreateLobby(request, session)
        is Requests.JoinLobby -> onJoinLobby(request, session)
        is Requests.LeaveLobby -> onLeaveLobby(request, session)
        is Requests.StartGame -> onStartGame(request, session)
        is Requests.Turn -> onTurn(request, session)
    }

    suspend fun onCreateLobby(request: Requests.CreateLobby, session: S)
    suspend fun onJoinLobby(request: Requests.JoinLobby, session: S)
    suspend fun onLeaveLobby(request: Requests.LeaveLobby, session: S)
    suspend fun onStartGame(request: Requests.StartGame, session: S)
    suspend fun onTurn(request: Requests.Turn, session: S)
}
