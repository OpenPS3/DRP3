package net.ps3utils.discord.rpc.utils

import mu.KotlinLogging
import java.net.HttpURLConnection
import java.net.URL

object ImageUtils {
    private val logger = KotlinLogging.logger { }

    fun getGameTDBImage(titleID: String): String {
        val regionMap = mapOf(
            'A' to "ZH",
            'E' to "EN",
            'H' to "US",
            'J' to "JA",
            'K' to "KO",
            'U' to "US"
        )

        if (titleID.length < 3) {
            logger.warn { "Invalid titleId: $titleID" }
            return titleID.lowercase()
        }

        val regionKey = titleID[2]
        val region = regionMap[regionKey]
        if (region == null) {
            logger.warn { "Unexpected key: $regionKey! Using default icons" }
            return titleID.lowercase()
        }

        val url = "https://art.gametdb.com/ps3/cover/$region/$titleID.jpg"

        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 3000
            connection.readTimeout = 3000
            val responseCode = connection.responseCode
            connection.disconnect()

            if (responseCode == 200) {
                logger.info { "using GameTDB: $url" }
                url
            } else {
                logger.warn { "No image found at $url, using Discord dev app image" }
                titleID.lowercase()
            }
        } catch (e: Exception) {
            println(e.message)
            titleID.lowercase()
        }
    }
}