package net.ps3utils.discord.rpc.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.util.regex.Pattern

class GatherDetails(val ip: String) {

    private var soup: Document? = null
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

    fun getThermals() {
        val anchor = soup?.selectFirst("a[href=/cpursx.ps3?up]")?.toString() ?: run {
            println("Can't get temperature info")
            return
        }
        val cpuMatch = Pattern.compile("CPU(.+?)C").matcher(anchor)
        val rsxMatch = Pattern.compile("RSX(.+?)C").matcher(anchor)

        if (cpuMatch.find() && rsxMatch.find()) {
            val cpu = cpuMatch.group(0)
            val rsx = rsxMatch.group(0)
            thermalData = "$cpu | $rsx"
        } else {
            println("Can't get temperature info")
        }
    }

    fun decideGameType() {
        isRetroGame = false
        if (soup?.selectFirst("a[target=_blank]") != null) {
            getPS3Details()
        }
        else if (soup?.selectFirst("a[href~=/dev_hdd0|/dev_usb00[0-9]/(PSXISO|PS2ISO)]") != null) {
            isRetroGame = true
        } else {
            name = "XMB"
            titleID = null
        }
    }

    fun getPS3Details() {
        val titleLink = soup?.selectFirst("a[target=_blank]")
        val nameElement = titleLink?.nextElementSibling()

        if (titleLink == null || nameElement == null) {
            println("Game info not found.")
            return
        }

        val titleText = titleLink.text()
        var gameName = nameElement.text().replace("\n", "")

        val versionPattern = Pattern.compile("(.+)[0-9]{2}\\.[0-9]{2}")
        val versionMatcher = versionPattern.matcher(gameName)
        if (versionMatcher.find()) {
            gameName = versionMatcher.group(1)
        }

        titleID = titleText
        name = gameName
    }
}