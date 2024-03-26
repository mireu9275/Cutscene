package kr.eme.plugin.cutscene.listeners

import kr.eme.plugin.cutscene.events.CutsceneEndEvent
import kr.eme.plugin.cutscene.events.CutsceneStartEvent
import kr.eme.plugin.cutscene.managers.ViewingPlayerManager
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object CutsceneListener: Listener {

    @EventHandler
    fun onCutsceneStart(event: CutsceneStartEvent) {
        ViewingPlayerManager.startViewing(event.player)
    }

    @EventHandler
    fun onCutsceneEnd(event: CutsceneEndEvent) {
        ViewingPlayerManager.stopViewing(event.player)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEntityEvent) {
        if (ViewingPlayerManager.isViewing(event.player) && event.rightClicked is ArmorStand) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager !is Player) return
        val player = event.damager as Player

        if (ViewingPlayerManager.isViewing(player) && event.entity is ArmorStand) {
            event.isCancelled = true
        }
    }
}