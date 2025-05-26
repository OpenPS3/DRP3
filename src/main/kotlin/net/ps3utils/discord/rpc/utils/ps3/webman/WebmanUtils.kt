package net.ps3utils.discord.rpc.utils.ps3.webman

import mu.KotlinLogging
import net.ps3utils.discord.rpc.utils.ps3.GameUtils
import net.ps3utils.discord.rpc.utils.ps3.GatherDetails
import net.ps3utils.discord.rpc.utils.ps3.SystemUtils

class WebmanUtils(ip: String) {
    private val gatherDetails = GatherDetails(ip)
    private val systemUtils = SystemUtils(gatherDetails)
    private val gameUtils = GameUtils(gatherDetails)
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
            logger.error {"Can't get PlayStation® 3 Home page, is WebmanMOD installed?"}
            thermalData = null
            gameName = null
            titleID = null
            isRetroGame = false
            currentActivity = "PlayStation®3 not connected"

            return false
        }

        systemUtils.getThermals()
        gameUtils.decideGameType()

        thermalData = gatherDetails.thermalData

        gameName = gatherDetails.name
        titleID = gatherDetails.titleID
        isRetroGame = gatherDetails.isRetroGame

        currentActivity = gameName?.let { "Playing $it" } ?: "Disconnected from PlayStation® 3"

        return true
    }
}