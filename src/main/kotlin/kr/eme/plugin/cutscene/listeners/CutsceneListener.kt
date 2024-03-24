package kr.eme.plugin.cutscene.listeners

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

object CutsceneListener: Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        return
    }
}