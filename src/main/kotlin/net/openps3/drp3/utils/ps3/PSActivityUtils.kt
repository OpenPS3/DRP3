package net.openps3.drp3.utils.ps3

import net.openps3.drp3.utils.common.Constants

class PSActivityUtils(private val dataLoader: PSDataLoader) {
    fun getCurrentActivity() {
        dataLoader.isRetroGame = false
        if (dataLoader.document?.selectFirst(Constants.TARGET_BLANK) != null) {
            dataLoader.systemUtils.getPS3Details()
        } else {
            dataLoader.name = null
            dataLoader.titleID = null
        }
    }
}