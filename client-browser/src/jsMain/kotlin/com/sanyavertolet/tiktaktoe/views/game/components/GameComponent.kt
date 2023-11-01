package com.sanyavertolet.tiktaktoe.views.game.components

import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Options
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.utils.FieldType
import mui.icons.material.Close
import mui.icons.material.RadioButtonUnchecked
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.ChildrenBuilder
import react.FC
import react.Props
import react.create
import web.cssom.Display
import web.cssom.JustifyContent
import web.cssom.px
import web.cssom.rem

private fun ChildrenBuilder.renderField(fieldSize: Int, field: FieldType, onClickCallback: (Position) -> Unit) {
    Container {
        sx {
            display = Display.flex
            justifyContent = JustifyContent.center
        }
        Stack {
            direction = responsive(StackDirection.column)
            divider = Divider.create { sx { borderWidth = 1.px } }
            for (i in 0 until fieldSize) {
                Stack {
                    direction = responsive(StackDirection.row)
                    divider = Divider.create {
                        sx { borderWidth = 1.px }
                        orientation = Orientation.vertical
                        flexItem = true
                    }
                    for (j in 0 until fieldSize) {
                        Position(i, j).let {
                            val marker = field[it]
                            renderCell(marker) { onClickCallback(it) }
                        }
                    }
                }
            }
        }
    }
}

private fun ChildrenBuilder.renderCell(marker: MarkerType?, onClickCallback: () -> Unit) {
    Button {
        sx {
            width = 4.rem
            height = 4.rem
        }
        onClick = { onClickCallback() }
        when (marker) {
            MarkerType.TIC -> Close()
            MarkerType.TAC -> RadioButtonUnchecked()
            else -> +" "
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
