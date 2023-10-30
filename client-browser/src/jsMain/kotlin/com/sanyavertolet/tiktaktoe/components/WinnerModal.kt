package com.sanyavertolet.tiktaktoe.components

import mui.material.*
import react.FC
import react.Props
import react.dom.aria.ariaDescribedBy
import react.router.useNavigate

external interface WinnerModalProps : Props {
    var winnerUserName: String?

    var isOpen: Boolean

    var closeModalCallback: () -> Unit
}

val winnerModal: FC<WinnerModalProps> = FC { props ->
    val navigate = useNavigate()
    Dialog {
        open = props.isOpen
        keepMounted = true
        onClose = { _, _ -> props.closeModalCallback().also { navigate(to="/") } }
        ariaDescribedBy = "alert-dialog-slide-description"
        fullWidth = true
        maxWidth = "xs"
        DialogTitle { +"Game has finished" }
        DialogContent {
            DialogContentText {
                id = "alert-dialog-slide-description"
                props.winnerUserName?.let { +"$it is winner!" } ?: +"Draw! No winners."
            }
        }
        DialogActions {
            Button {
                onClick = { props.closeModalCallback() }
                +"Go to main page"
            }
        }
    }
}