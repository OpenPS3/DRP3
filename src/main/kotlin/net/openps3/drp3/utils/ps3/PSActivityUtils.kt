package net.openps3.drp3.utils.ps3

import net.openps3.drp3.utils.common.Queries

class PSActivityUtils(private val dataLoader: PSDataLoader) {
    fun getCurrentActivity() {
        dataLoader.isRetroGame = false
        if (dataLoader.document?.selectFirst(Queries.TARGET_BLANK) != null) {
            dataLoader.systemUtils.getPS3Details()
        }
        else if (dataLoader.document?.selectFirst(Queries.DIRECTORIES_REGEX) != null) {
            dataLoader.isRetroGame = true
        } else {
            dataLoader.name = "XMB™"
            dataLoader.titleID = null
        }
    }
}