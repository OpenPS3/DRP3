package net.openps3.drp3.utils.ps3.webman

import mu.KotlinLogging
import net.openps3.drp3.utils.ps3.PSActivityUtils
import net.openps3.drp3.utils.ps3.PSDataLoader
import net.openps3.drp3.utils.ps3.SystemUtils

class WebmanUtils(ip: String) {
    private val dataLoader = PSDataLoader(ip)
    private val systemUtils = SystemUtils(dataLoader)
    private val gameUtils = PSActivityUtils(dataLoader)
    private val logger = KotlinLogging.logger { }

    var thermalData: String? = null
        private set
    var gameName: String? = null
        private set
    var titleID: String? = null
        private set
    var isRetroGame: Boolean = false
        private set
    var currentActivity: String? = null
        private set

    fun fetchDetails(): Boolean {
        if (!dataLoader.getHtml()) {
            logger.error { "Can't get PlayStation®3 Home page, is WebmanMOD installed?" }
            thermalData = null
            gameName = null
            titleID = null
            isRetroGame = false
            currentActivity = "PlayStation®3 not connected"

            return false
        }

        systemUtils.getThermals()
        gameUtils.getCurrentActivity()

        thermalData = dataLoader.thermalData
        gameName = dataLoader.name
        titleID = dataLoader.titleID
        isRetroGame = dataLoader.isRetroGame
        currentActivity = gameName?.let { "Playing $it" } ?: "Disconnected from PlayStation® 3"

        return true
    }
}