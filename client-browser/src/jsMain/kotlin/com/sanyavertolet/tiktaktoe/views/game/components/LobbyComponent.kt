package com.sanyavertolet.tiktaktoe.views.game.components

import com.sanyavertolet.tiktaktoe.game.Options
import js.core.jso
import mui.material.*
import mui.material.styles.TypographyVariant
import mui.system.Stack
import mui.system.StackDirection
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.create
import react.useState
import web.cssom.AlignSelf
import web.cssom.rem
import web.navigator.navigator

val lobbyComponent: FC<LobbyComponentProps> = FC { props ->
    val (isCopyNotificationShown, setIsCopyNotificationShown) = useState(false)
    Stack {
        spacing = responsive(2)
        Typography {
            variant = TypographyVariant.h4
            +"Greetings, ${props.userName}!"
        }
        Stack {
            spacing = responsive(4)
            direction = responsive(StackDirection.row)
            Typography {
                variant = TypographyVariant.h4
                +"Lobby code:"
            }
            Chip {
                sx {
                    minWidth = 4.rem
                    alignSelf = AlignSelf.center
                }
                label = Typography.create { +props.lobbyCode }
                variant = ChipVariant.outlined
                onClick = {
                    navigator.clipboard.writeText(props.lobbyCode)
                    setIsCopyNotificationShown(true)
                }
            }
        }

        Snackbar {
            open = isCopyNotificationShown
            onClose = { _, _ -> setIsCopyNotificationShown(false) }
            autoHideDuration = 1500
            message = Typography.create { +"Copied to clipboard" }
            anchorOrigin = jso {
                vertical = SnackbarOriginVertical.bottom
                horizontal = SnackbarOriginHorizontal.right
            }
        }

        props.options?.let {
            Typography {
                variant = TypographyVariant.h6
                +"Field size: ${it.fieldSize}"
            }

            Typography {
                variant = TypographyVariant.h6
                +"Win condition: ${it.winCondition}"
            }
        }

        Typography {
            variant = TypographyVariant.h6
            +(props.opponentName?.let { "Opponent name: $it" } ?: "Waiting for an opponent...")
        }

        if(props.isHost && props.opponentName != null) {
            Button {
                variant = ButtonVariant.outlined
                onClick = { props.startGame() }
                +"Go!"
            }
        }
    }
}

external interface LobbyComponentProps : Props {
    var userName: String
    var options: Options?
    var opponentName: String?
    var startGame: () -> Unit
    var isHost: Boolean
    var lobbyCode: String
}
