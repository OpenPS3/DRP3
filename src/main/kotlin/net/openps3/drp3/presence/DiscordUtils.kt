package net.openps3.drp3.presence

import de.jcm.discordgamesdk.Core
import net.openps3.drp3.DRP3Instance

class DiscordUtils(val instance: DRP3Instance) {
    val webmanUtils = instance.webmanUtils
    val activityBuilder = ActivityBuilder(instance)

    fun checkActivity(core: Core) {
        instance.logger.info { "Checking activity..." }
        val success = webmanUtils.fetchDetails()
        if (success) {
            instance.logger.info { "Presence updated | ${webmanUtils.gameName} | ${webmanUtils.titleID}" }
            activityBuilder.buildActivity(core)
        } else activityBuilder.buildInactiveActivity(core)
    }

}