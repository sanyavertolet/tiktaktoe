package com.sanyavertolet.tiktaktoe.game

enum class MarkerType(val ch: String) {
    TAC("0"),
    TIC("X"),
    ;
    fun another() = when(this) {
        TIC -> TAC
        TAC -> TIC
    }
}
