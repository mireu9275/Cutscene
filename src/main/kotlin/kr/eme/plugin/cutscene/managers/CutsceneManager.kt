package kr.eme.plugin.cutscene.managers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import kr.eme.plugin.cutscene.main
import kr.eme.plugin.cutscene.objects.CutsceneData
import kr.eme.plugin.cutscene.objects.Transition
import kr.eme.plugin.cutscene.protocolManager
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

object CutsceneManager {
    private val cutscenes = mutableListOf<CutsceneData>()
    fun getCutscene(name: String): CutsceneData? = cutscenes.find { it.name == name }
    fun addCutscene(name: String) {
        val cutscene = CutsceneData(name)
        cutscenes.add(cutscene)
    }
    fun addSequence(name: String, seq: Int, location: Location) {
        val cutscene = getCutscene(name)
        cutscene?.addSequence(seq, location)
    }
    fun getSequenceLocation(name: String, seq: Int): Location? {
        val cutscene = getCutscene(name)
        return cutscene?.getSequenceLocation(seq)
    }

    fun getTransition(name: String): List<Transition> {
        val cutscene = getCutscene(name)
        return cutscene?.transitions ?: emptyList()
    }
    fun addTransition(name: String, transition: Transition) {
        val cutscene = getCutscene(name)
        cutscene?.addTransition(transition)
    }

    fun playCutscene(player: Player, name: String) {
        val cutscene = getCutscene(name) ?: return
        val startLoc = cutscene.getSequenceLocation(0) ?: return

        // 시작 위치에 아머 스탠드 소환
        val armorStand = player.world.spawn(startLoc, ArmorStand::class.java).apply {
            isVisible = false
            isCustomNameVisible = false
            isInvulnerable = true
            setGravity(false)
            isMarker = true
        }

        // 플레이어 시점을 아머 스탠드로 변경
        switchToViewpoint(player, armorStand.entityId)

        // 아머 스탠드를 이동시키면서 컷신 재생
        cutscene.transitions.forEach { transition ->
            val startLocation = cutscene.getSequenceLocation(transition.startSeq) ?: return@forEach
            val endLoc = cutscene.getSequenceLocation(transition.endSeq) ?: return@forEach
            val time = transition.time

            player.sendMessage("Moving from sequence ${transition.startSeq} to ${transition.endSeq} in $time ticks")

            val deltaX = (endLoc.x - startLocation.x) / time
            val deltaY = (endLoc.y - startLocation.y) / time
            val deltaZ = (endLoc.z - startLocation.z) / time

            for (i in 1..time) {
                val newLoc = Location(
                    startLocation.world,
                    startLocation.x + deltaX * i,
                    startLocation.y + deltaY * i,
                    startLocation.z + deltaZ * i
                )
                armorStand.teleport(newLoc)
                Thread.sleep(50)
            }
        }
        // 컷신 종료 후 아머 스탠드 제거
        armorStand.remove()
    }

    private fun switchToViewpoint(player: Player, entityId: Int) {
        try {
            //오류 발견 : 서버 내에 해당 엔티티가 삭제되었는데도 진행이 됨,

            //EntityId의 카메라 패킷 가져오기
            val cameraPacket = PacketContainer(PacketType.Play.Server.CAMERA).apply {
                integers.write(0, entityId)
            }
            //플레이어의 시점을 EntityId로 변경
            protocolManager.sendServerPacket(player, cameraPacket)
        } catch (e: Exception) {
            main.warn("switchToViewpoint Exception : $e")
        }
    }
}