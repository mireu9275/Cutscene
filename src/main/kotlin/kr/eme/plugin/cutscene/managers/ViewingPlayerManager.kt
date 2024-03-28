package kr.eme.plugin.cutscene.managers

import kr.eme.plugin.cutscene.Cutscene
import kr.eme.plugin.cutscene.events.CutsceneEndEvent
import kr.eme.plugin.cutscene.events.CutsceneStartEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object ViewingPlayerManager {
    private val viewingPlayers = mutableMapOf<Player, String>()

    fun isViewing(player: Player): Boolean = player in viewingPlayers

    fun startViewing(player: Player, cutsceneName: String) {
        viewingPlayers[player] = cutsceneName
        val startEvent = CutsceneStartEvent(player, cutsceneName)
        Bukkit.getPluginManager().callEvent(startEvent)
    }
    fun stopViewing(player: Player) {
        viewingPlayers.remove(player)
        val endEvent = CutsceneEndEvent(player)
        Bukkit.getPluginManager().callEvent(endEvent)
    }
    fun getCutsceneName(player: Player): String? = viewingPlayers[player]
}