package com.sanyavertolet.tiktaktoe.views.game

import com.sanyavertolet.tiktaktoe.BrowserWebSocketClient
import com.sanyavertolet.tiktaktoe.components.errorModal
import com.sanyavertolet.tiktaktoe.components.winnerModal
import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.handlers.*
import com.sanyavertolet.tiktaktoe.utils.useFieldFromState
import com.sanyavertolet.tiktaktoe.utils.useGameRouteLoaderData
import com.sanyavertolet.tiktaktoe.utils.useOnce
import com.sanyavertolet.tiktaktoe.views.game.components.gameComponent
import com.sanyavertolet.tiktaktoe.views.game.components.lobbyComponent
import js.core.jso
import kotlinx.coroutines.Dispatchers
import react.FC
import react.router.useNavigate
import react.useEffect
import react.useMemo
import react.useState

val gameView = FC {
    val (userNameFromUrl, lobbyCodeFromUrl, optionsFromUrl) = useGameRouteLoaderData()
    val navigate = useNavigate()

    val (options, setOptions) = useState(optionsFromUrl)
    val (markerType, setMarkerType) = useState<MarkerType?>(null)
    val (field, setField) = useFieldFromState()
    val (isMyTurn, setIsMyTurn) = useState(false)

    val (isWinnerModalShown, setIsWinnerModalShown) = useState(false)
    val (winnerUserName, setWinnerUserName) = useState<String?>(null)
    val (opponentName, setOpponentName) = useState<String?>(null)
    val (opponentTurn, setOpponentTurn) = useState<Position?>(null)

    val (errorMessage, setErrorMessage) = useState<String?>(null)

    errorModal {
        this.isOpen = errorMessage != null
        this.errorMessage = errorMessage.orEmpty()
        this.closeModalCallback = { setErrorMessage(null).also { navigate("/", jso { replace = true }) } }
    }

    useEffect(opponentTurn) {
        opponentTurn?.let { opponentTurn ->
            setField { it + (opponentTurn to markerType!!.another()) }
            setOpponentTurn(null)
        }
    }

    val onPlayerJoined: PlayerJoinedCallback = { anotherUserName, fieldSize, winCondition ->
        setOptions { previousOptions ->
            previousOptions ?: Options(fieldSize, winCondition)
        }
        setOpponentName(anotherUserName)
    }

    val onGameStarted: GameStartedCallback = { whoIsFirst ->
        val marker = if (userNameFromUrl == whoIsFirst) MarkerType.TIC else MarkerType.TAC
        setMarkerType(marker)
        setIsMyTurn(userNameFromUrl == whoIsFirst)
    }

    val onGameFinished: GameFinishedCallback = { whoIsTheWinner ->
        setWinnerUserName(whoIsTheWinner)
        setIsWinnerModalShown(true)
    }

    val onPlayerLeft: PlayerLeftCallback = {
        setWinnerUserName(userNameFromUrl)
        setIsWinnerModalShown(true)
    }

    val onTurn: TurnCallback = { position, userName ->
        if (userName != userNameFromUrl) {
            setOpponentTurn(position)
            setIsMyTurn(true)
        }
    }

    val onError: ErrorCallback = { error, _ ->
        setErrorMessage(error)
    }

    console.log(errorMessage)

    val jsNotificationHandler = useMemo {
        JsNotificationHandler(
            onPlayerJoined,
            onGameStarted,
            onGameFinished,
            onPlayerLeft,
            onTurn,
            onError,
        )
    }

    val client = useMemo { BrowserWebSocketClient(userNameFromUrl, lobbyCodeFromUrl, jsNotificationHandler, Dispatchers.Default) }
    useOnce { client.startSession(options) }

    winnerModal {
        this.isOpen = isWinnerModalShown
        this.winnerUserName = winnerUserName
        this.closeModalCallback = { setIsWinnerModalShown(false).also { navigate("/") } }
    }

    when {
        markerType == null -> lobbyComponent {
            this.userName = userNameFromUrl
            this.options = options
            this.opponentName = opponentName
            this.startGame = client::sendGameStartRequest
            this.isHost = optionsFromUrl != null
            this.lobbyCode = lobbyCodeFromUrl
        }
        else -> gameComponent {
            this.isMyTurn = isMyTurn
            this.opponentName = opponentName!!
            this.options = options!!
            this.field = field
            this.makeTurn = { position ->
                if (field[position] == null && isMyTurn) {
                    client.sendTurnRequest(position)
                    setField { it + (position to markerType) }
                    setIsMyTurn(false)
                }
            }
        }
    }
}
