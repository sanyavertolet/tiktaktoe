package com.sanyavertolet.tiktaktoe.views.welcome.components

import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.utils.getMD5
import com.sanyavertolet.tiktaktoe.utils.targetString
import com.sanyavertolet.tiktaktoe.utils.useOnce
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.onChange
import react.useState
import web.cssom.rem

external interface CreateComponentProps : Props {
    var onGoButtonPressed: (String, Options) -> Unit
    var hostName: String
    var isGoButtonDisabled: Boolean
}

val createComponent: FC<CreateComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")
    val (options, setOptions) = useState(Options.default)

    useOnce {
        setLobbyCode(props.hostName.getMD5(6))
    }

    Stack {
        sx { paddingTop = 1.rem }
        spacing = responsive(2)

        TextField {
            id = "lobby-code"
            size = Size.small
            label = ReactNode("Lobby code")
            variant = FormControlVariant.outlined
            value = lobbyCode
            disabled = true
            onChange = {
                setLobbyCode(it.targetString)
            }
        }

        TextField {
            id = "field-size"
            size = Size.small
            label = ReactNode("Field size")
            variant = FormControlVariant.outlined
            value = options.fieldSize.toString()
            onChange = { event ->
                event.targetString
                    .tryIntCoerceAtLeast(Options.MIN_FIELD_SIZE, Options.MAX_FIELD_SIZE)
                    ?.let { newFieldSize -> setOptions { oldOptions -> oldOptions.copy(fieldSize = newFieldSize) } }
            }
        }

        TextField {
            id = "win-condition"
            size = Size.small
            label = ReactNode("Win condition")
            variant = FormControlVariant.outlined
            value = options.winCondition.toString()
            onChange = { event ->
                event.targetString
                    .tryIntCoerceAtLeast(Options.MIN_WIN_CONDITION, Options.MAX_WIN_CONDITION)
                    ?.let { newWinCondition -> setOptions { oldOptions -> oldOptions.copy(winCondition = newWinCondition) } }
            }
        }

        Button {
            variant = ButtonVariant.outlined
            onClick = { props.onGoButtonPressed(lobbyCode, options) }
            disabled = props.isGoButtonDisabled
            +"Go!"
        }
    }
}

private fun String.tryIntCoerceAtLeast(minimalValue: Int, maximalValue: Int) = takeIf { it.isNotBlank() }
    ?.toIntOrNull()
    ?.coerceIn(minimalValue..maximalValue)
