package com.sanyavertolet.tiktaktoe.views.welcome

import com.sanyavertolet.tiktaktoe.utils.targetString
import com.sanyavertolet.tiktaktoe.views.welcome.components.createComponent
import com.sanyavertolet.tiktaktoe.views.welcome.components.joinComponent
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.ReactNode
import react.dom.onChange
import react.router.useNavigate
import react.useState
import web.cssom.JustifyContent
import web.cssom.Margin
import web.cssom.rem
import web.cssom.vh

private enum class Mode(val prettyString: String) {
    CREATE("Create"),
    JOIN("Join"),
    ;
}

val welcomeView = FC {
    val navigate = useNavigate()
    val (userName, setUserName) = useState("")
    val (lobbyCode, setLobbyCode) = useState("")

    val (createOrJoin, setCreateOrJoin) = useState(Mode.CREATE)

    Container {
        maxWidth = "sm"
        Box {
            sx {
                paddingTop = 2.rem
                height = 50.vh
            }
            Stack {
                spacing = responsive(2)

                TextField {
                    id = "user-name"
                    size = Size.small
                    label = ReactNode("Name")
                    variant = FormControlVariant.outlined
                    value = userName
                    onChange = {
                        setUserName(it.targetString)
                    }
                }

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

                Divider {
                    sx {
                        margin = Margin(1.rem, 0.rem)
                    }
                }

                ToggleButtonGroup {
                    sx { justifyContent = JustifyContent.center }
                    exclusive = true
                    color = ToggleButtonGroupColor.primary
                    value = createOrJoin
                    onChange = { _, newCreateOrChange ->
                        (newCreateOrChange as Mode?)?.let { setCreateOrJoin(it) }
                    }
                    ToggleButton {
                        value = Mode.CREATE
                        +Mode.CREATE.prettyString
                    }
                    ToggleButton {
                        value = Mode.JOIN
                        +Mode.JOIN.prettyString
                    }
                }
            }

            when (createOrJoin) {
                Mode.CREATE -> createComponent {
                    onGoButtonPressed = {
                        navigate("/$userName/$lobbyCode?c=${it.winCondition}&s=${it.fieldSize}")
                    }
                }
                Mode.JOIN -> joinComponent {
                    onGoButtonPressed = {
                        navigate("/$userName/$lobbyCode")
                    }
                }
            }
        }
    }
}
