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

    fun getTransition(name: String, index: Int): Transition? {
        val cutscene = getCutscene(name)
        return cutscene?.getTransition(index)
    }
    fun addTransition(name: String, index: Int, transition: Transition) {
        val cutscene = getCutscene(name)
        cutscene?.addTransition(index, transition)
    }

    fun playCutscene(player: Player, name: String) {
        val cutscene = getCutscene(name) ?: return
        val armorStands = mutableListOf<ArmorStand>()

        // 각 순번마다 아머 스탠드 소환
        cutscene.getSequenceLocations().forEach { (seq, location) ->
            val armorStand = player.world.spawn(location, ArmorStand::class.java).apply {
                isVisible = false
                isCustomNameVisible = false
                isInvulnerable = true
                setGravity(false)
                isMarker = true
            }
            armorStands.add(armorStand)
        }

        // 시작 위치의 아머 스탠드로 시점 변경
        val startArmorStand = armorStands.firstOrNull()
        if (startArmorStand != null) switchToViewpoint(player, startArmorStand.entityId)

        // 트랜잭션 확인
        val transitions = cutscene.getTransitions()
        if (transitions.isEmpty()) {
            val startArmorStand = armorStands.firstOrNull()
            if (startArmorStand != null) {
                switchToViewpoint(player, startArmorStand.entityId)
                val time = cutscene.getTransition(0)?.time ?: 20L // 첫 번째 트랜지션의 시간 사용, 없으면 기본값 20틱 사용
                Thread.sleep(time * 50L)
            }
        } else {
            // 트랜잭션이 있는 경우, 시점 간 이동 처리
            val sortedTransitions = transitions.toSortedMap()
            sortedTransitions.forEach { (index, transition) ->
                val startArmorStand = armorStands.getOrNull(transition.startSeq)
                val endArmorStand = armorStands.getOrNull(transition.endSeq)
                if (startArmorStand != null && endArmorStand != null) {
                    val startLocation = startArmorStand.location
                    val endLocation = endArmorStand.location
                    val time = transition.time

                    // 시작 시점으로 이동
                    switchToViewpoint(player, startArmorStand.entityId)
                    Thread.sleep(time * 50L) // 틱 단위로 변환하여 대기

                    // 끝 시점으로 이동
                    val nextIndex = sortedTransitions.keys.firstOrNull { it > index }
                    if (nextIndex != null) {
                        val nextTransition = sortedTransitions[nextIndex]
                        if (nextTransition != null) {
                            val moveTime = nextTransition.time
                            moveViewpoint(player, startLocation, endLocation, moveTime)
                        }
                    }
                }
            }
        }
        // 컷신 종료 후 아머 스탠드 제거
        armorStands.forEach { it.remove() }
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

    private fun moveViewpoint(player: Player, startLocation: Location, endLocation: Location, time: Long) {
        val deltaX = (endLocation.x - startLocation.x) / time
        val deltaY = (endLocation.y - startLocation.y) / time
        val deltaZ = (endLocation.z - startLocation.z) / time

        for (i in 1..time) {
            val newLoc = Location(
                startLocation.world,
                startLocation.x + deltaX * i,
                startLocation.y + deltaY * i,
                startLocation.z + deltaZ * i
            )
            player.teleport(newLoc)
            Thread.sleep(50)
        }
    }
}