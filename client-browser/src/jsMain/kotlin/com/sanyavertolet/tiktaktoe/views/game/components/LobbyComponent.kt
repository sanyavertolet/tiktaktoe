package com.sanyavertolet.tiktaktoe.views.game.components

import com.sanyavertolet.tiktaktoe.game.Options
import mui.material.Button
import mui.material.ButtonVariant
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.Stack
import mui.system.responsive
import react.FC
import react.Props

val lobbyComponent: FC<LobbyComponentProps> = FC { props ->
    Stack {
        spacing = responsive(2)
        Typography {
            variant = TypographyVariant.h4

            +"Greetings, ${props.userName}!"
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
}
