package kr.eme.plugin.cutscene.commands

import kr.eme.plugin.cutscene.main
import kr.eme.plugin.cutscene.managers.CutsceneManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CutsceneManager: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            if (sender !is Player) return false
            if (args.isEmpty()) {
                usage(sender)
                return true
            }

            when (args[0]) {
                "exit" -> exitCutscene(sender)
                "view" -> enterCutscene(args, sender)
                "set" -> setCutscene(args, sender)
                else -> usage(sender)
            }

            return true
        } catch (e: Exception)  {
            main.warn("onCommand Exception : $e")
            return false
        }
    }

    /**
     * Usage
     *
     * @param player
     */
    private fun usage(player: Player) {
        player.sendMessage(
            "/cutscene set [컷신명] : 컷신을 설정합니다.\n" +
            "/cutscene view [컷신명] : 컷신을 봅니다.\n" +
            "/cutscene exit : 컷신에서 나갑니다.\n"
        )
    }

    /**
     * 만들어진 컷신을 보여줍니다.
     *
     * @param name
     * @param player
     */
    private fun enterCutscene(args: Array<out String>, player: Player) {
        try {
            if (args.size != 2) {
                player.sendMessage("명령어를 다시 확인해주세요. /Cutscene view [컷신명]")
                return
            }
            val name = args[1]
            val cameraEntityId = CutsceneManager.getCamera(name)?.entityId
            if (cameraEntityId == null) {
                player.sendMessage("볼 수 없는 컷신입니다.")
                return
            }
            CutsceneManager.switchToViewpoint(player,cameraEntityId)
            player.sendMessage("정상적으로 $name 컷신으로 변경되었습니다.")
        } catch (e: Exception) {
            main.warn("enterView Exception : $e")
        }
    }

    /**
     * 컷신을 나갑니다.
     *
     * @param player
     */
    private fun exitCutscene(player: Player) {
        try {
            CutsceneManager.switchToViewpoint(player, player.entityId)
            player.sendMessage("정상적으로 컷신에서 벗어났습니다.")
        } catch (e: Exception) {
            main.warn("exitView Exception : $e")
        }
    }

    /**
     * 컷신을 설정합니다.
     *
     * @param name
     * @param player
     */
    private fun setCutscene(args: Array<out String>, player: Player) {
        if (args.size != 2) {
            player.sendMessage("명령어를 다시 확인해주세요. /Cutscene set [컷신명]")
            return
        }
       val name = args[1]
        try {
           CutsceneManager.createCameraEntity(name, player)
       } catch (e: Exception) {
           main.warn("setView Exception : $e")
       }
    }
}