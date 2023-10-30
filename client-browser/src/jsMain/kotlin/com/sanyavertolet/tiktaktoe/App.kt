package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.tiktaktoe.routing.createRouter
import com.sanyavertolet.tiktaktoe.themes.themeProvider
import com.sanyavertolet.tiktaktoe.views.errorView
import mui.material.Container
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.create
import react.router.RouterProvider
import web.cssom.TextAlign

const val SERVER_URL = BrowserWebSocketClient.URL

val app = FC {
    themeProvider {
        Container {
            maxWidth = "md"
            Typography {
                sx { textAlign = TextAlign.center }
                variant = TypographyVariant.h2
                +"Tik-Tak-Toe game"
            }
            RouterProvider {
                router = createRouter()
                fallbackElement = errorView.create()
            }
        }
    }
}

fun main() = rootWrapper(app)
