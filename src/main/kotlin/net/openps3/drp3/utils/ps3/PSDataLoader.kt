package net.openps3.drp3.utils.ps3

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class PSDataLoader(val ip: String) {
    val systemUtils = SystemUtils(this)
    var document: Document? = null
    var thermalData: String? = null
    var name: String? = null
    var titleID: String? = null
    var isRetroGame: Boolean = false
    var isConnected: Boolean = false

    private val client = HttpClient(CIO)

    suspend fun pingConsole(): Boolean {
        // Sends a notification followed by beep to XMB
        val url = "http://$ip/notify.ps3mapi?msg=Connected+to+Discord&icon=0&snd=1"

        return try {
            client.get(url)
            true
        } catch (e: Exception) {
            println("Error while sending message to PS3, skipping.")
            false
        }
    }

    fun getHtml(): Boolean = runBlocking {
        if (!isConnected) {
            pingConsole()
            isConnected = true
        }

        val url = "http://$ip/cpursx.ps3?/sman.ps3"

        return@runBlocking try {
            val html = client.get(url).bodyAsText()
            document = Jsoup.parse(html)
            true
        } catch (e: Exception) {
            println("Error while searching HTML: ${e.message}")
            false
        }
    }
}