package kr.eme.plugin.cutscene.managers

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import kr.eme.plugin.cutscene.main
import kr.eme.plugin.cutscene.protocolManager
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

object CutsceneManager {
    private val cameraMap = HashMap<String, ArmorStand>()

    /**
     * 해당 이름의 카메라가 있는지 확인합니다.
     *
     * @param name
     * @return
     */
    private fun containsCamera(name: String): Boolean = cameraMap.containsKey(name)

    /**
     * 현재 등록되어있는 카메라를 가져옵니다.
     *
     * @param name
     */
    fun getCamera(name: String) = cameraMap[name]

    /**
     * 현재 등록되어있는 카메라 리스트를 가져옵니다.
     *
     * @return List<String>
     */
    fun getCameraList() : List<String> = cameraMap.keys.toList()

    /**
     * 카메라 엔티티 생성
     *
     * @param name
     * @param player
     */
    fun createCameraEntity(name: String, player: Player) {
        //만약 이미 동일한 이름으로 카메라 엔티티가 존재 하다면
        //해당 위치의 엔티티를 삭제 후 재실행함.
        if (containsCamera(name)) {
            removeCamera(name)
        }
        //플레이어의 위치를 불러옵니다.
        val location = player.location
        location.y += 1;

        //플레이어의 위치에 아머스탠드(마커화)엔티티 를 생성합니다.
        player.world.spawn(location, ArmorStand::class.java).apply {
            isVisible = false               // 보이지 않게 설정
            isCustomNameVisible = false     // 이름표가 보이지 않게 설정
            isInvulnerable = true           // 무적 설정
            setGravity(false)               // 중력 영향 받지 않음 설정
            isMarker = true                 // 클릭 받지 않음 설정
        }.also { cameraMap[name] = it }
    }

    /**
     * 현재 등록되어있는 카메라를 삭제합니다.
     *
     * @param name
     */
    private fun removeCamera(name: String) {
        //cameraMap 에 없으면 return
        if(!containsCamera(name)) return
        cameraMap[name]?.let {
            // 게임 월드에서 엔티티 삭제
            it.remove()
            // cameraMap 에서 삭제
            cameraMap.remove(name)
        }
    }

    /**
     * 선택한 엔티티로 시점을 옮깁니다.
     *
     * @param player
     * @param entityId
     */
    fun switchToViewpoint(player: Player, entityId: Int) {
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