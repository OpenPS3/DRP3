package net.openps3.drp3.utils.ps3

import mu.KotlinLogging
import net.openps3.drp3.utils.common.Constants
import java.util.regex.Pattern

class SystemUtils(private val dataLoader: PSDataLoader) {
    private val logger = KotlinLogging.logger {  }

    fun getPS3Details() {
        val titleLink = dataLoader.document?.selectFirst(Constants.TARGET_BLANK)
        val nameElement = titleLink?.nextElementSibling()
        lateinit var gameName: String

        if (titleLink == null || nameElement == null) {
            logger.info { "Game info not found." }
            return
        }

        val titleText = titleLink.text()
        gameName = nameElement.text().replace("\n", "")

        val versionPattern = Pattern.compile("(.+)[0-9]{2}\\.[0-9]{2}")
        val versionMatcher = versionPattern.matcher(gameName)
        if (versionMatcher.find()) {
            gameName = versionMatcher.group(1)
        }

        logger.info { "Received $gameName from webMAN"}
        dataLoader.titleID = titleText
        dataLoader.name = gameName
    }

    fun getThermals() {
        val thermalSection = dataLoader.document?.selectFirst(Constants.CPU_RSX_PAGE)?.toString() ?: run {
            logger.info { "Can't get temperature info" }
            return
        }

        val cpuMatch = Pattern.compile("CPU(.+?)C").matcher(thermalSection)
        val rsxMatch = Pattern.compile("RSX(.+?)C").matcher(thermalSection)

        if (cpuMatch.find() && rsxMatch.find()) {
            val cpu = cpuMatch.group(0)
            val rsx = rsxMatch.group(0)
            dataLoader.thermalData = "$cpu | $rsx"
        } else {
            logger.info { "Can't get temperature info" }
        }
    }
}