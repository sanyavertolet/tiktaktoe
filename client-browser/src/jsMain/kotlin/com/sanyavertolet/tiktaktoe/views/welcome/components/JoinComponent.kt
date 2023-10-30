package com.sanyavertolet.tiktaktoe.views.welcome.components

import com.sanyavertolet.tiktaktoe.utils.targetString
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.ReactNode
import react.dom.onChange
import react.useState
import web.cssom.rem

external interface JoinComponentProps : Props {
    var onGoButtonPressed: (String) -> Unit
}

val joinComponent: FC<JoinComponentProps> = FC { props ->
    val (lobbyCode, setLobbyCode) = useState("")
    Stack {
        sx { paddingTop = 1.rem }
        spacing = responsive(2)

        TextField {
            id = "lobby-code"
            size = Size.small
            label = ReactNode("Lobby code")
            variant = FormControlVariant.outlined
            value = lobbyCode
            onChange = {
                setLobbyCode(it.targetString)
            }
        }

        Button {
            variant = ButtonVariant.outlined
            onClick = { props.onGoButtonPressed(lobbyCode) }
            +"Go!"
        }
    }
}
