package kr.eme.plugin.cutscene.commands

import kr.eme.plugin.cutscene.events.CutsceneEndEvent
import kr.eme.plugin.cutscene.events.CutsceneStartEvent
import kr.eme.plugin.cutscene.main
import kr.eme.plugin.cutscene.managers.CutsceneManager
import kr.eme.plugin.cutscene.managers.ViewingPlayerManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import javax.swing.text.View

object CutsceneCommand: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        try {
            if (command.name != "cutscene") return false
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
        if (args.size != 2) {
            player.sendMessage("명령어를 다시 확인해주세요. /Cutscene view [컷신명]")
            return
        }
        val name = args[1]
        val cutscene = CutsceneManager.getCutscene(name)
        if (cutscene == null) {
            player.sendMessage("볼 수 없는 컷신입니다.")
            return
        }
        if (ViewingPlayerManager.isViewing(player)) {
            player.sendMessage("이미 컷신을 보고 있습니다.")
            return
        }
        ViewingPlayerManager.startViewing(player, name)
        CutsceneManager.playCutscene(player, name)
        player.sendMessage("정상적으로 $name 컷신으로 변경되었습니다.")
    }

    /**
     * 컷신을 나갑니다.
     *
     * @param player
     */
    private fun exitCutscene(player: Player) {
        if (!ViewingPlayerManager.isViewing(player)) {
            player.sendMessage("컷신을 보고 있지 않습니다.")
            return
        }
        ViewingPlayerManager.stopViewing(player)
        player.sendMessage("정상적으로 컷신에서 벗어났습니다.")
    }

    /**
     * 컷신을 설정합니다.
     *
     * @param name
     * @param player
     */
    private fun setCutscene(args: Array<out String>, player: Player) {
        if (args.size != 4) {
            player.sendMessage("명령어를 다시 확인해주세요. /Cutscene set [컷신명] [위치명] [순번]")
            return
        }
        val name = args[1]
        val seq = args[3].toIntOrNull()
        if (seq == null) {
            player.sendMessage("순번은 숫자로 입력해주세요.")
            return
        }
        CutsceneManager.addCutscene(name)
        CutsceneManager.addSequence(name, seq, player.location)
        player.sendMessage("$name 컷신의 $seq 번째 위치가 설정되었습니다.")
    }
}