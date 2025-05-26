package net.ps3utils.discord.rpc.utils.ps3

import net.ps3utils.discord.rpc.utils.ps3.GatherDetails
import net.ps3utils.discord.rpc.utils.common.Queries

class GameUtils(private val gather: GatherDetails) {
    fun decideGameType() {
        gather.isRetroGame = false
        if (gather.soup?.selectFirst(Queries.TARGET_BLANK) != null) {
            gather.systemUtils.getPS3Details()
        }
        else if (gather.soup?.selectFirst(Queries.DIRECTORIES_REGEX) != null) {
            gather.isRetroGame = true
        } else {
            gather.name = "XMB"
            gather.titleID = null
        }
    }
}