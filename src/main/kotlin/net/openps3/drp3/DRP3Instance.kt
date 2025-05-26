package net.openps3.drp3

import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import de.jcm.discordgamesdk.LogLevel
import mu.KotlinLogging
import net.openps3.drp3.presence.DiscordUtils
import net.openps3.drp3.utils.common.ConfigFile
import net.openps3.drp3.utils.ps3.webman.WebmanUtils
import kotlin.use

class DRP3Instance(ip: String, val config: ConfigFile) {
    val webmanUtils = WebmanUtils(ip)
    val logger = KotlinLogging.logger {  }
    val presence = DiscordUtils(this)

    var lastTitleID: String? = null
    var lastActivity: String? = null

    fun start(clientId: Long) {
        CreateParams().use { params ->
            params.setClientID(clientId)
            params.setFlags(CreateParams.getDefaultFlags())

            Core(params).use { core ->
                while (true) {
                    core.setLogHook(LogLevel.INFO, { level, message ->
                        when (level) {
                            LogLevel.INFO -> logger.info { message }
                            LogLevel.WARN -> logger.warn { message }
                            LogLevel.ERROR -> logger.error { message }
                            LogLevel.VERBOSE -> { }
                            LogLevel.DEBUG -> logger.debug {  }
                        }
                    })

                    presence.checkActivity(core)
                    core.runCallbacks()
                    Thread.sleep(config.updateInterval)
                }
            }
        }
    }
}