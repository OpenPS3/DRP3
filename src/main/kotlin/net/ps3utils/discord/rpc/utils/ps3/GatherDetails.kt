package net.ps3utils.discord.rpc.utils.ps3

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class GatherDetails(val ip: String) {
    val systemUtils = SystemUtils(this)
    var soup: Document? = null
    var thermalData: String? = null
    var name: String? = null
    var titleID: String? = null
    var isRetroGame: Boolean = false

    private val client = HttpClient(CIO)

    fun getHtml(): Boolean = runBlocking {
        val url = "http://$ip/cpursx.ps3?/sman.ps3"
        return@runBlocking try {
            val html = client.get(url).bodyAsText()
            soup = Jsoup.parse(html)
            true
        } catch (e: Exception) {
            println("Error while searching HTML: ${e.message}")
            false
        }
    }
}