package com.sanyavertolet.tiktaktoe.components

import mui.material.*
import react.FC
import react.Props
import react.dom.aria.ariaDescribedBy
import react.router.useNavigate

external interface ErrorModalProps : Props {
    var errorMessage: String

    var isOpen: Boolean

    var closeModalCallback: () -> Unit
}

val errorModal: FC<ErrorModalProps> = FC { props ->
    val navigate = useNavigate()
    Dialog {
        open = props.isOpen
        keepMounted = true
        onClose = { _, _ -> props.closeModalCallback().also { navigate(to="/") } }
        ariaDescribedBy = "alert-dialog-slide-description"
        fullWidth = true
        maxWidth = "xs"
        DialogTitle { +"Error" }
        DialogContent {
            DialogContentText {
                id = "alert-dialog-slide-description"
                props.errorMessage.let { +it }
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