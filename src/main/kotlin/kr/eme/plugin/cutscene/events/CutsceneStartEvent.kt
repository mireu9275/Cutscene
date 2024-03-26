package kr.eme.plugin.cutscene.events

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class CutsceneStartEvent(val player: Player) : Event() {
    companion object {
        private val handlers = HandlerList()
        @JvmStatic
        fun getHandlerList(): HandlerList = handlers
    }
    override fun getHandlers(): HandlerList = handlers
}