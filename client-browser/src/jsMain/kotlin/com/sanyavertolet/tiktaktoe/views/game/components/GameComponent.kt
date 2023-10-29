package com.sanyavertolet.tiktaktoe.views.game.components

import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.FieldType
import mui.icons.material.Close
import mui.icons.material.RadioButtonUnchecked
import mui.material.*
import mui.system.sx
import react.ChildrenBuilder
import react.FC
import react.Props
import web.cssom.Border
import web.cssom.LineStyle
import web.cssom.rem

private fun ChildrenBuilder.renderField(fieldSize: Int, field: FieldType, onClickCallback: (Position) -> Unit) {
    Container {
        Stack {
            direction = "row".asDynamic()
            for (i in 0 until fieldSize) {
                Stack {
                    dir = "column".asDynamic()
                    for (j in 0 until fieldSize) {
                        val position = Position(i, j)
                        val marker = field[Position(i, j)]

                        Button {
                            variant = ButtonVariant.outlined
                            sx {
                                width = 2.rem
                                height = 2.rem
                                border = Border(0.01.rem, LineStyle.solid)
                            }
                            onClick = { onClickCallback(position) }
                            when (marker) {
                                MarkerType.TIC -> Close()
                                MarkerType.TAC -> RadioButtonUnchecked()
                                else -> +" "
                            }
                        }
                    }
                }
            }
        }
    }
}

val gameComponent: FC<GameComponentProps> = FC { props ->
    Typography {
        sx {
            padding = 2.rem
        }
        +if (props.isMyTurn) {
            "It's your turn now."
        } else {
            "It's ${props.opponentName} turn now."
        }
    }

    renderField(props.options.fieldSize, props.field) {
        props.makeTurn(it)
    }
}

external interface GameComponentProps: Props {
    var options: Options
    var field: FieldType
    var makeTurn: (Position) -> Unit
    var isMyTurn: Boolean
    var opponentName: String
}
