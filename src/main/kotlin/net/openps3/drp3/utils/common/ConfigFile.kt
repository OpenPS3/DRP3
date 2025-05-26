package net.openps3.drp3.utils.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConfigFile(
    val clientId: String,
    @SerialName("console_ip")
    val consoleIp: String,
    val updateInterval: Long,
    @SerialName("show_temp")
    val showTemp: Boolean,
    @SerialName("show_cover")
    val showCover: Boolean
)
