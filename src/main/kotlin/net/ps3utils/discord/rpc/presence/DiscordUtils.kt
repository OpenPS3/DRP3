package net.ps3utils.discord.rpc.presence

import PS3InfoFetcher
import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.activity.Activity
import net.ps3utils.discord.rpc.utils.ImageUtils.getGameTDBImage
import java.time.Instant
import kotlin.use

class DiscordUtils(val instance: PS3InfoFetcher) {
    val webmanUtils = instance.webmanUtils

    fun checkActivity(core: Core) {
        println("Checking activity...")
        val success = webmanUtils.fetchDetails()
        if (success) {
            instance.logger.onUpdate()
            buildActivity(core)
        }
    }

    private fun buildActivity(core: Core) {
        val activityDetails = if (webmanUtils.gameName.isNullOrBlank()) "On XMB" else "Playing ${webmanUtils.gameName}"

        if (activityDetails != instance.lastActivity || webmanUtils.titleID != instance.lastTitleID) {
            instance.lastActivity = activityDetails
            instance.lastTitleID = webmanUtils.titleID

            Activity().use { activity ->
                if (webmanUtils.titleID == "NPIA00025") {
                    activity.details = "Looking PlayStation Store"
                    activity.state = webmanUtils.thermalData
                    activity.timestamps().start = Instant.now()
                    activity.assets().largeImage = "psstore_icon"
                    activity.assets().largeText = "PlayStation Store"

                    core.activityManager().updateActivity(activity)
                    return
                }

                activity.details = activityDetails
                activity.state = webmanUtils.thermalData

                activity.timestamps().start = Instant.now()

                val coverUrl = if (webmanUtils.gameName.isNullOrBlank()) {
                    "default_image_key"
                } else {
                    webmanUtils.titleID?.let { getGameTDBImage(it) } ?: "default_image_key"
                }

                activity.assets().largeImage = coverUrl
                activity.assets().largeText = webmanUtils.titleID ?: "Unknown ID"

                core.activityManager().updateActivity(activity)
            }
        }
    }
}