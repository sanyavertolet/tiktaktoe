package com.sanyavertolet.tiktaktoe.handlers

import com.sanyavertolet.tiktaktoe.messages.Notifications

interface ErrorHandler {
    fun printError(errorMessage: String) = println(errorMessage)

    fun onError(error: Notifications.Error)

    fun onError(error: Throwable)
}
