package com.sanyavertolet.tiktaktoe.exceptions

interface ErrorHandler<S : Any> {
    suspend fun onError(exception: GameException, session: S)
}
