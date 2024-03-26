package kr.eme.plugin.cutscene.managers

import kr.eme.plugin.cutscene.main
import java.io.File

object FileManager {
    private val configFile = File(main.dataFolder, "config.yml")

    /**
     * 설정 파일에서 값을 가져옵니다.
     *
     */
    fun getConfig() = main.config

    /**
     * 기본 config 파일이 없을 시 생성
     *
     */
    fun setupConfig() {
        main.saveDefaultConfig()
    }

    /**
     * 변경된 설정을 저장합니다.
     *
     */
    fun saveConfig() {
        main.saveConfig()
    }

    /**
     * config 파일을 다시 로드합니다.
     *
     */
    fun reloadConfig() {
        main.reloadConfig()
    }
}