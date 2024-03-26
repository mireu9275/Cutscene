package kr.eme.plugin.cutscene.managers

import kr.eme.plugin.cutscene.events.CutsceneEndEvent
import kr.eme.plugin.cutscene.events.CutsceneStartEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ViewingPlayerManager {
    private val viewingPlayers = mutableSetOf<Player>()
    fun isViewing(player: Player): Boolean = player in viewingPlayers
    fun startViewing(player: Player) {
        viewingPlayers.add(player)
    }
    fun stopViewing(player: Player) {
        viewingPlayers.remove(player)
    }
}