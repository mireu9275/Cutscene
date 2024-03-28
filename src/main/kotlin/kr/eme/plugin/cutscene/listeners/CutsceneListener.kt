package kr.eme.plugin.cutscene.listeners

import kr.eme.plugin.cutscene.events.CutsceneEndEvent
import kr.eme.plugin.cutscene.events.CutsceneStartEvent
import kr.eme.plugin.cutscene.main
import kr.eme.plugin.cutscene.managers.ViewingPlayerManager
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent

object CutsceneListener : Listener {

    @EventHandler
    fun onCutsceneStart(event: CutsceneStartEvent) {
        main.info("${event.player.name} 님이 [${event.cutsceneName}] 컷신에 들어갔습니다.")
    }

    @EventHandler
    fun onCutsceneEnd(event: CutsceneEndEvent) {
        main.info("${event.player.name} 님이 컷신에서 빠져나갔습니다.")
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEntityEvent) {
        val player = event.player
        if (ViewingPlayerManager.isViewing(player) && event.rightClicked is ArmorStand) {
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