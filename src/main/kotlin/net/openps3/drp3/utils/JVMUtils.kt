package net.openps3.drp3.utils

import java.io.File
import net.openps3.drp3.DRP3Launcher

fun copyFromJar(input: String, output: String) {
    val inputStream = DRP3Launcher::class.java.getResourceAsStream(input) ?: return
    File(output).writeBytes(inputStream.readAllBytes())
}