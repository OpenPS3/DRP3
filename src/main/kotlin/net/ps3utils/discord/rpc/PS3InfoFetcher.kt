import de.jcm.discordgamesdk.Core
import de.jcm.discordgamesdk.CreateParams
import net.ps3utils.discord.rpc.presence.DiscordUtils
import net.ps3utils.discord.rpc.utils.LogUtils
import net.ps3utils.discord.rpc.utils.WebmanUtils
import kotlin.use

class PS3InfoFetcher(private val ip: String) {
    val webmanUtils = WebmanUtils(ip)
    val logger = LogUtils(webmanUtils)
    val presence = DiscordUtils(this)

    var lastTitleID: String? = null
    var lastActivity: String? = null

    fun start(clientId: Long) {
        CreateParams().use { params ->
            params.setClientID(clientId)
            params.setFlags(CreateParams.getDefaultFlags())
            Core(params).use { core ->
                while (true) {
                    presence.checkActivity(core)
                    core.runCallbacks()
                    Thread.sleep(20000)
                }
            }
        }
    }
}