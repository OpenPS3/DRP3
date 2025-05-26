package net.ps3utils.discord.rpc.utils

import java.io.File
import net.ps3utils.discord.rpc.RPCLauncher

fun copyFromJar(input: String, output: String) {
    val inputStream = RPCLauncher::class.java.getResourceAsStream(input) ?: return
    File(output).writeBytes(inputStream.readAllBytes())
}