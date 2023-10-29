package com.sanyavertolet.tiktaktoe.components

import react.FC
import react.Props
import react.router.useNavigate

external interface WinnerModalProps : Props {
    var winnerUserName: String?

    var isOpen: Boolean
}

val winnerModal: FC<WinnerModalProps> = FC { props ->
    val navigate = useNavigate()
    if (props.isOpen) {
        // todo: show pretty modal
        console.log(
            props.winnerUserName?.let {
                "$it is winner!"
            } ?: "Draw! No winners."
        )
        navigate(to = "/")
    }
}