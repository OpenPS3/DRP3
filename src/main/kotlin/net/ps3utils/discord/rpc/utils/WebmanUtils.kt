package net.ps3utils.discord.rpc.utils

import mu.KotlinLogging

class WebmanUtils(val ip: String) {
    private val gatherDetails = GatherDetails(ip)
    private val logger = KotlinLogging.logger {  }

    var thermalData: String? = null
        private set
    var gameName: String? = null
        private set
    var titleID: String? = null
        private set
    var isRetroGame: Boolean = false
        private set

    var currentActivity: String? = null

    fun fetchDetails(): Boolean {
        if (!gatherDetails.getHtml()) {
            logger.error {"Can't get PS3 Home page, is WebmanMOD installed?"}
            thermalData = null
            gameName = null
            titleID = null
            isRetroGame = false
            currentActivity = null
            return false
        }

        gatherDetails.getThermals()
        gatherDetails.decideGameType()

        thermalData = gatherDetails.thermalData

        gameName = gatherDetails.name
        titleID = gatherDetails.titleID
        isRetroGame = gatherDetails.isRetroGame

        currentActivity = gameName?.let { "Playing $it" } ?: "Disconnected from PlayStation 3"

        return true
    }
}