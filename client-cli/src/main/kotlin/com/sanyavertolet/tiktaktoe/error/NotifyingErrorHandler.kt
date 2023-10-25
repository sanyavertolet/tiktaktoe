package com.sanyavertolet.tiktaktoe.error

import com.sanyavertolet.tiktaktoe.ErrorHandler
import com.sanyavertolet.tiktaktoe.messages.Notifications

class NotifyingErrorHandler(private val onExit: (Int) -> Unit) : ErrorHandler {
    override fun onError(error: Notifications.Error) {
        printError(error.errorMessage)
        onExit(1)
    }

    override fun onError(error: Throwable) {
        printError(error.message ?: error.localizedMessage ?: error.stackTraceToString())
        onExit(1)
    }
}
