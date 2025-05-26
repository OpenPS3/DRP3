package net.openps3.drp3

import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import java.io.File
import java.util.Scanner
import kotlin.system.exitProcess
import kotlinx.serialization.hocon.Hocon
import net.openps3.drp3.utils.HoconUtils.decodeFromString
import net.openps3.drp3.utils.common.ConfigFile
import net.openps3.drp3.utils.copyFromJar


object DRP3Launcher {
    @OptIn(ExperimentalSerializationApi::class)
    val HOCON = Hocon { useArrayPolymorphism = true }

    @JvmStatic
    fun main(args: Array<String>) {
        val configFile = checkConfigFile()
        val config = readConfigFile<ConfigFile>(configFile)

        DRP3Instance(config.consoleIp, config).start(config.clientId.toLong())
    }

    private fun checkConfigFile(): File {
        val configFile = File(System.getProperty("conf") ?: "settings.conf")

        if (!configFile.exists()) {
            println(
                """                            
               Welcome to Discord Rich Presence for PlayStation® 3!

                What's your PlayStation® 3 IP address?
                To find your PlayStation® 3 IP address
                Navigate to Settings > Network Settings > Internet Connection Test and then check the "Connection Status" for the IP address.
"""
            )

            val scanner = Scanner(System.`in`)
            print("Insert your PlayStation® 3 IP: 192.168.")
            val ip = "192.168." + scanner.nextLine()

            copyFromJar("/settings.conf", configFile.path)

            val lines = configFile.readLines().toMutableList()
            var replaced = false

            for (i in lines.indices) {
                if (lines[i].startsWith("console_ip")) {
                    lines[i] = "console_ip = $ip"
                    replaced = true
                    break
                }
            }

            if (!replaced) {
                lines.add("console_ip = $ip")
            }

            configFile.writeText(lines.joinToString("\n"))

            println("Configuration saved. Please restart the program.")
            exitProcess(1)
        }

        return configFile
    }

    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> readConfigFile(file: File): T {
        try {
            val json = file.readText()
            return HOCON.decodeFromString<T>(json)
        } catch (e: IOException) {
            e.printStackTrace()
            exitProcess(1)
        }
    }
}
