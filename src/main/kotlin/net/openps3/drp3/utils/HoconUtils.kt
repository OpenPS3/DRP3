package net.openps3.drp3.utils

import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig

object HoconUtils {
    @OptIn(ExperimentalSerializationApi::class)
    inline fun <reified T> Hocon.decodeFromString(string: String): T =
        decodeFromConfig(ConfigFactory.parseString(string).resolve())
}