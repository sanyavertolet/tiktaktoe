package com.sanyavertolet.tiktaktoe.exceptions

class GameException(message: String, val isCritical: Boolean) : Exception(message)
