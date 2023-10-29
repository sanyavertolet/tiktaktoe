package com.sanyavertolet.tiktaktoe

import com.sanyavertolet.kotlinjspreview.RootWrapper
import react.FC
import react.create
import react.dom.client.createRoot
import web.dom.document

@RootWrapper
fun rootWrapper(fc: FC<*>) {
    val div = document.getElementById("root") ?: return
    createRoot(div).render(
        fc.create(),
    )
}
