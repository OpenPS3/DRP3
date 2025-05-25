package net.ps3utils.discord.rpc.utils

class LogUtils(val webmanUtils: WebmanUtils) {
    fun onUpdate() {
        println("\n----[UPDATING]----")
        println("Temperatures: ${webmanUtils.thermalData}")
        println("Game name: ${webmanUtils.gameName}")
        println("Title ID: ${webmanUtils.titleID}\n")
    }
}