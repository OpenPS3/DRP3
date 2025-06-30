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

            val titleData = mapOf(
                Constants.PLAYSTATION_STORE_ID to Triple("Looking at PlayStation® Store", Constants.PS_STORE_LOGO_KEY, "PlayStation® Store"),
                Constants.PSTVPLUS_ID to Triple("Watching PlayStation®TV Plus", Constants.PSTVPLUS_LOGO_KEY, "PlayStation®TV Plus"),
                Constants.YOUTUBE_ID to Triple("Watching YouTube", Constants.YOUTUBE_LOGO_KEY, "YouTube")
            )

            Activity().use { activity ->
                val data = titleData[webmanUtils.titleID]
                val isSpecial = data != null

                activity.details = data?.first ?: activityDetails
                activity.state = if (instance.config.showTemp) webmanUtils.thermalData else webmanUtils.titleID
                activity.timestamps().start = Instant.now()

                activity.assets().largeImage = when {
                    data != null -> data.second
                    instance.config.showCover -> webmanUtils.titleID?.let { getGameTDBImage(it) } ?: Constants.DEFAULT_IMAGE_KEY
                    else -> Constants.DEFAULT_IMAGE_KEY
                }

                activity.assets().largeText = data?.third ?: (webmanUtils.titleID ?: "Unknown ID")

                core.activityManager().updateActivity(activity)

                if (isSpecial) return
            }
        }
    }
}