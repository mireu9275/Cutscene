package kr.eme.plugin.cutscene

import com.comphenix.protocol.ProtocolLibrary
import kr.eme.plugin.cutscene.commands.CutsceneCommand
import kr.eme.plugin.cutscene.listeners.CutsceneListener
import org.bukkit.plugin.java.JavaPlugin

class Cutscene: JavaPlugin() {
    override fun onEnable() {
        main = this
        // 서버가 활성화 될 때 설정해둔 컷신 위치에 엔티티들이 소환돼 있어야 함.
        protocolManager = ProtocolLibrary.getProtocolManager()
        getCommand("Cutscene")?.setExecutor(CutsceneCommand)
        server.pluginManager.registerEvents(CutsceneListener, main)

        info("Cutscene plugin enable")
    }
    override fun onDisable() {
        info("Cutscene plugin disable")
    }

    fun warn(message: String) { logger.warning(message) }
    fun info(message: String) { logger.info(message) }
}