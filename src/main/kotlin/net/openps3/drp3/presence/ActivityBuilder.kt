package net.openps3.drp3.presence

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.activity.Activity
import net.openps3.drp3.DRP3Instance
import net.openps3.drp3.utils.ImageUtils.getGameTDBImage
import net.openps3.drp3.utils.common.Constants
import java.time.Instant

class ActivityBuilder(val instance: DRP3Instance) {
    val webmanUtils = instance.webmanUtils

    fun buildInactiveActivity(core: Core) {
        Activity().use { activity ->
            activity.details = "Waiting PlayStation®3."
            activity.state = webmanUtils.thermalData
            activity.timestamps().start = Instant.now()
            activity.assets().largeImage = Constants.DEFAULT_IMAGE_KEY

            core.activityManager().updateActivity(activity)
        }
    }

    fun buildActivity(core: Core) {
        val activityDetails = if (webmanUtils.gameName.isNullOrBlank()) "On XMB™" else "Playing ${webmanUtils.gameName}"

        if (activityDetails != instance.lastActivity || webmanUtils.titleID != instance.lastTitleID) {
            instance.lastActivity = activityDetails
            instance.lastTitleID = webmanUtils.titleID

            Activity().use { activity ->
                if (webmanUtils.titleID == Constants.PLAYSTATION_STORE_ID) {
                    activity.details = "Looking at PlayStation® Store"
                    activity.state = if (instance.config.showTemp) webmanUtils.thermalData else webmanUtils.titleID
                    activity.timestamps().start = Instant.now()
                    activity.assets().largeImage = Constants.PS_STORE_LOGO_KEY
                    activity.assets().largeText = "PlayStation® Store"

                    core.activityManager().updateActivity(activity)
                    return
                }

                activity.details = activityDetails
                activity.state = if (instance.config.showTemp) webmanUtils.thermalData else webmanUtils.titleID
                activity.timestamps().start = Instant.now()

                if (instance.config.showCover) {
                    activity.assets().largeImage = if (webmanUtils.gameName.isNullOrBlank()) {
                        Constants.DEFAULT_IMAGE_KEY
                    } else {
                        webmanUtils.titleID?.let { getGameTDBImage(it) } ?: Constants.DEFAULT_IMAGE_KEY
                    }

                } else activity.assets().largeImage = Constants.DEFAULT_IMAGE_KEY

                    activity.assets().largeText = webmanUtils.titleID ?: "Unknown ID"

                core.activityManager().updateActivity(activity)
            }
        }
    }
}