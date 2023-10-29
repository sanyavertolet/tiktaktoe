package com.sanyavertolet.tiktaktoe.themes

import mui.material.CssBaseline
import mui.system.ThemeProvider
import react.FC
import react.PropsWithChildren
import react.useState

val themeProvider: FC<PropsWithChildren> = FC { props ->
    val (selectedTheme, _) = useState(Themes.Light)
    ThemeProvider {
//        theme = selectedTheme

        CssBaseline()
        +props.children
    }
}
