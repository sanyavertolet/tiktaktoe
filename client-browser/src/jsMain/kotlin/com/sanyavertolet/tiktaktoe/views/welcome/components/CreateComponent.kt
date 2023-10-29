package com.sanyavertolet.tiktaktoe.views.welcome.components

import com.sanyavertolet.tiktaktoe.game.Options
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

external interface CreateComponentProps : Props {
    var onGoButtonPressed: (Options) -> Unit
}

val createComponent: FC<CreateComponentProps> = FC { props ->
    val (options, setOptions) = useState(Options.default)
    Stack {
        sx { paddingTop = 1.rem }
        spacing = responsive(2)

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
            onClick = { props.onGoButtonPressed(options) }
            +"Go!"
        }
    }
}

private fun String.tryIntCoerceAtLeast(minimalValue: Int, maximalValue: Int) = takeIf { it.isNotBlank() }
    ?.toIntOrNull()
    ?.coerceIn(minimalValue..maximalValue)
