package com.sanyavertolet.tiktaktoe.exceptions

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.multiplayer.websockets.sendError
import io.ktor.websocket.*
import org.slf4j.LoggerFactory

class NotifyingWebSocketErrorHandler : ErrorHandler<DefaultWebSocketSession> {
    override suspend fun onError(exception: GameException, session: DefaultWebSocketSession) {
        val message = exception.message ?: exception.stackTraceToString()
        logger.warn(message)
        if (exception.isCritical) {
            session.close(CloseReason(CloseReason.Codes.INTERNAL_ERROR, message))
        } else {
            session.sendError(Notifications.Error(message, false, exception.stackTraceToString()))
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NotifyingWebSocketErrorHandler::class.java)
    }
}
