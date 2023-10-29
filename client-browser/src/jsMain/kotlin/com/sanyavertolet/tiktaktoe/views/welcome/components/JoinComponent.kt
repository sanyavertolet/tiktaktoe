package com.sanyavertolet.tiktaktoe.views.welcome.components

import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import web.cssom.rem

external interface JoinComponentProps : Props {
    var onGoButtonPressed: () -> Unit
}

val joinComponent: FC<JoinComponentProps> = FC { props ->
    Stack {
        sx { paddingTop = 1.rem }
        spacing = responsive(2)

        Button {
            variant = ButtonVariant.outlined
            onClick = { props.onGoButtonPressed() }
            +"Go!"
        }
    }
}
