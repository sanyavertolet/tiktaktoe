package com.sanyavertolet.tiktaktoe.utils

import com.sanyavertolet.tiktaktoe.game.MarkerType
import com.sanyavertolet.tiktaktoe.game.Position
import com.sanyavertolet.tiktaktoe.routing.GameRouteLoaderData
import react.StateInstance
import react.router.useLoaderData
import react.useState

fun useGameRouteLoaderData(): GameRouteLoaderData {
    val loaderData = useLoaderData()
    return loaderData as GameRouteLoaderData
}

typealias FieldType = Map<Position, MarkerType>

fun useFieldFromState(): StateInstance<FieldType> = useState(emptyMap())

fun useOnceAction(): (() -> Unit) -> Unit {
    val (isFirstRender, setFirstRender) = useState(true)
    return { action ->
        if (isFirstRender) {
            action()
            setFirstRender(false)
        }
    }
}

fun useOnce(action: () -> Unit) {
    val useOnceAction = useOnceAction()
    useOnceAction {
        action()
    }
}
