import net.ps3utils.discord.rpc.utils.GatherDetails

class PS3InfoFetcher(private val ip: String) {
    private val gatherDetails = GatherDetails(ip)

    var thermalData: String? = null
        private set
    var gameName: String? = null
        private set
    var titleID: String? = null
        private set
    var isRetroGame: Boolean = false
        private set

    @Volatile
    private var running = false

    fun fetchDetails(): Boolean {
        if (!gatherDetails.getHtml()) {
            println("Can't get PS3 Home page, is WebmanMOD installed?")
            return false
        }

        gatherDetails.getThermals()
        gatherDetails.decideGameType()

        thermalData = gatherDetails.thermalData
        gameName = gatherDetails.name
        titleID = gatherDetails.titleID
        isRetroGame = gatherDetails.isRetroGame

        return true
    }

    fun startLoop(intervalMillis: Long = 5000) {
        if (running) {
            println("Loop already running")
            return
        }
        running = true

        Thread {
            while (running) {
                val success = fetchDetails()
                if (success) {
                    println("\n")
                    println("---------------------------")
                    println("Temperatures: $thermalData")
                    println("Game name: $gameName")
                    println("Title ID: $titleID")
                    println("Is Retro Game? $isRetroGame")
                    println("---------------------------")
                }
                Thread.sleep(intervalMillis)
            }
        }.start()
    }
}

