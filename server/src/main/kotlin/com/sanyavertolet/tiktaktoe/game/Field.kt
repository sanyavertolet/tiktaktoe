package com.sanyavertolet.tiktaktoe.game

class Field(private val boardSize: Int, private val rowWinCount: Int) {
    private val field = Array(boardSize) { Array(boardSize) { PlayerType.NONE } }

    operator fun get(position: Position) = field[position.x][position.y]
    operator fun set(position: Position, playerType: PlayerType) {
        field[position.x][position.y] = playerType
    }

    operator fun set(position: Position, player: Player<*>) = set(position, player.type)

    fun whoWins(): Pair<Boolean, PlayerType> {
        fun checkDirection(iStart: Int, jStart: Int, iStep: Int, jStep: Int, player: PlayerType): Boolean {
            return (0 until rowWinCount).all { field[iStart + it * iStep][jStart + it * jStep] == player }
        }

        var gameInProgress = false

        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                val player = field[i][j]

                if (player == PlayerType.NONE) {
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

        return !gameInProgress to PlayerType.NONE
    }
}
