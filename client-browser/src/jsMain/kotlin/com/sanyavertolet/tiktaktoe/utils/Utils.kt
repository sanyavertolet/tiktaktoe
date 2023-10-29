package com.sanyavertolet.tiktaktoe.utils

import react.dom.events.FormEvent
import web.html.HTMLDivElement

val FormEvent<HTMLDivElement>.targetString
    get() = asDynamic().target.value as String
