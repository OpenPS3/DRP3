package net.ps3utils.discord.rpc.presence

import RPCInstance
import de.jcm.discordgamesdk.Core

class DiscordUtils(val instance: RPCInstance) {
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