package net.openps3.drp3.presence

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.activity.Activity
import net.openps3.drp3.DRP3Instance
import net.openps3.drp3.utils.ImageUtils.getGameTDBImage
import java.time.Instant

class ActivityBuilder(val instance: DRP3Instance) {
    val webmanUtils = instance.webmanUtils

    fun buildInactiveActivity(core: Core) {
        Activity().use { activity ->
            activity.details = "PlayStation®3 not connected."
            activity.state = webmanUtils.thermalData
            activity.timestamps().start = Instant.now()
            activity.assets().largeImage = "default_image_key"

            core.activityManager().updateActivity(activity)
        }
    }

    fun buildActivity(core: Core) {
        val activityDetails = if (webmanUtils.gameName.isNullOrBlank()) "On XMB™" else "Playing ${webmanUtils.gameName}"

        if (activityDetails != instance.lastActivity || webmanUtils.titleID != instance.lastTitleID) {
            instance.lastActivity = activityDetails
            instance.lastTitleID = webmanUtils.titleID

            Activity().use { activity ->
                if (webmanUtils.titleID == "NPIA00025") {
                    activity.details = "Looking at PlayStation® Store"
                    activity.state = if (instance.config.showTemp) webmanUtils.thermalData else webmanUtils.titleID
                    activity.timestamps().start = Instant.now()
                    activity.assets().largeImage = "psstore_logo"
                    activity.assets().largeText = "PlayStation® Store"

                    core.activityManager().updateActivity(activity)
                    return
                }

                activity.details = activityDetails
                activity.state = if (instance.config.showTemp) webmanUtils.thermalData else webmanUtils.titleID
                activity.timestamps().start = Instant.now()

                if (instance.config.showCover) {
                    activity.assets().largeImage = if (webmanUtils.gameName.isNullOrBlank()) {
                        "default_image_key"
                    } else {
                        webmanUtils.titleID?.let { getGameTDBImage(it) } ?: "default_image_key"
                    }

                } else activity.assets().largeImage = "default_image_key"

                    activity.assets().largeText = webmanUtils.titleID ?: "Unknown ID"

                core.activityManager().updateActivity(activity)
            }
        }
    }
}