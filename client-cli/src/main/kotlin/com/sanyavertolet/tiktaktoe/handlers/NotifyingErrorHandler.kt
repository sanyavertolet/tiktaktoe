package com.sanyavertolet.tiktaktoe.handlers

import com.sanyavertolet.tiktaktoe.messages.Notifications

class NotifyingErrorHandler(private val onExit: (Int) -> Unit) : ErrorHandler {
    override fun onError(error: Notifications.Error) = printError(error.errorMessage).also {
        if (error.isCritical) {
            onExit(1)
        }
    }

    override fun onError(error: Throwable) {
        printError(error.message ?: error.localizedMessage ?: error.stackTraceToString())
        onExit(1)
    }
}
