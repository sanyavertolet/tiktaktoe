package com.sanyavertolet.tiktaktoe.utils

import io.ktor.utils.io.core.*
import org.kotlincrypto.hash.md.MD5
import react.dom.events.FormEvent
import web.html.HTMLDivElement

val FormEvent<HTMLDivElement>.targetString
    get() = asDynamic().target.value as String

const val LOCAL_STORAGE_USERNAME_KEY = "ls_username"

@OptIn(ExperimentalStdlibApi::class)
fun String.getMD5(n: Int) = MD5().digest(toByteArray()).toHexString().take(n)
