package com.sanyavertolet.tiktaktoe.themes

import js.core.jso
import mui.material.PaletteMode.Companion.light
import mui.material.styles.TypographyOptions
import mui.material.styles.TypographyVariant
import mui.system.createTheme
import web.cssom.atrule.maxWidth
import web.cssom.integer
import web.cssom.px
import web.cssom.rem

object Themes {
    val Light = createTheme(
        jso {
            palette = jso { mode = light }
            typography = TypographyOptions {
                fontWeight = integer(500)

                TypographyVariant.h6 {
                    fontSize = 1.5.rem

                    media(maxWidth(599.px)) {
                        fontSize = 1.25.rem
                    }
                }
            }
        },
    )
}
