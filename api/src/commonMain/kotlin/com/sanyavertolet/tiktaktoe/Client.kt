package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.messages.Notifications
import com.sanyavertolet.tiktaktoe.messages.Requests

interface Client {
    suspend fun startSessionAndRequest(
        url: String,
        onNotificationReceived: (Notifications) -> Unit,
        onConnectionSend: () -> Requests,
        onClose: (String?) -> Unit,
    )

    fun sendRequest(request: Requests)
}
