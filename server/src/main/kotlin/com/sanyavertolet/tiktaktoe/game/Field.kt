package com.sanyavertolet.tiktaktoe.game

typealias FieldType = Array<Array<MarkerType?>>

class Field(private val boardSize: Int, private val rowWinCount: Int) {
    private val field: FieldType = Array(boardSize) { Array(boardSize) { null } }

    operator fun get(position: Position) = field[position.x][position.y]
    operator fun set(position: Position, markerType: MarkerType) {
        field[position.x][position.y] = markerType
    }

    operator fun set(position: Position, player: Player<*>) = set(position, player.type)

    fun whoWins(): Pair<Boolean, MarkerType?> {
        fun checkDirection(iStart: Int, jStart: Int, iStep: Int, jStep: Int, player: MarkerType): Boolean {
            return (0 until rowWinCount).all { field[iStart + it * iStep][jStart + it * jStep] == player }
        }

        var gameInProgress = false

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val player = field[i][j]

                if (player == null) {
                    gameInProgress = true
                    continue
                }

                if (j <= boardSize - rowWinCount && checkDirection(i, j, 0, 1, player)) {
                    return true to player
                }
                if (i <= boardSize - rowWinCount && checkDirection(i, j, 1, 0, player)) {
                    return true to player
                }
                if (i <= boardSize - rowWinCount && j >= rowWinCount - 1 && checkDirection(i, j, 1, -1, player)) {
                    return true to player
                }
                if (i <= boardSize - rowWinCount && j <= boardSize - rowWinCount && checkDirection(i, j, 1, 1, player)) {
                    return true to player
                }
            }
        }

        return !gameInProgress to null
    }
}
