package com.sanyavertolet.tiktaktoe

import com.github.ajalt.clikt.core.subcommands
import com.sanyavertolet.tiktaktoe.clikt.Create
import com.sanyavertolet.tiktaktoe.clikt.Join
import com.sanyavertolet.tiktaktoe.clikt.Test
import com.sanyavertolet.tiktaktoe.clikt.World

fun main(args: Array<String>) = World().subcommands(Create(), Join(), Test()).main(args)
