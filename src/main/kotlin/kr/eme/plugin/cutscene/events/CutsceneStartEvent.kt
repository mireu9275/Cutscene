package kr.eme.plugin.cutscene.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CutsceneStartEvent(
    val player: Player,
    val cutsceneName: String
) : Event() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
    override fun getHandlers(): HandlerList = handlerList
}