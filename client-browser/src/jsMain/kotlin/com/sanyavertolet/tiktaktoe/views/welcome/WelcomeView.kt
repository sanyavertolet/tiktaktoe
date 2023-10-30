package com.sanyavertolet.tiktaktoe.views.welcome

import com.sanyavertolet.tiktaktoe.utils.LOCAL_STORAGE_USERNAME_KEY
import com.sanyavertolet.tiktaktoe.utils.targetString
import com.sanyavertolet.tiktaktoe.views.welcome.components.browseComponent
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
import web.storage.localStorage

private enum class Mode(val prettyString: String) {
    CREATE("Create"),
    JOIN("Join"),
    BROWSE("Browse"),
    ;
}

val welcomeView = FC {
    val navigate = useNavigate()
    val (userName, setUserName) = useState(localStorage.getItem(LOCAL_STORAGE_USERNAME_KEY).orEmpty())
    val (isUserNameValid, setIsUserNameValid) = useState(localStorage.getItem(LOCAL_STORAGE_USERNAME_KEY).orEmpty().isNameValid())
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
                    error = !isUserNameValid
                    helperText = ReactNode("Username should have at least 2 chars and at most 15.").takeIf { !isUserNameValid }
                    onChange = {
                        setUserName(it.targetString)
                        setIsUserNameValid(it.targetString.isNameValid())
                        localStorage.setItem(LOCAL_STORAGE_USERNAME_KEY, it.targetString)
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
                    ToggleButton {
                        value = Mode.BROWSE
                        +Mode.BROWSE.prettyString
                    }
                }
            }

            Box {
                sx { paddingTop = 2.rem }
                when (createOrJoin) {
                    Mode.CREATE -> createComponent {
                        hostName = userName
                        isGoButtonDisabled = !isUserNameValid
                        onGoButtonPressed = { lobbyCode, options ->
                            if (isUserNameValid) {
                                navigate("/$userName/$lobbyCode?c=${options.winCondition}&s=${options.fieldSize}")
                            }
                        }
                    }

                    Mode.JOIN -> joinComponent {
                        isGoButtonDisabled = !isUserNameValid
                        onGoButtonPressed = { lobbyCode ->
                            if (isUserNameValid) {
                                navigate("/$userName/$lobbyCode")
                            }
                        }
                    }

                    Mode.BROWSE -> browseComponent {
                        onGoButtonPressed = { selectedLobbyCode ->
                            if (isUserNameValid) {
                                navigate("/$userName/$selectedLobbyCode")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun String.isNameValid() = length in 2..15 && isNotBlank()
