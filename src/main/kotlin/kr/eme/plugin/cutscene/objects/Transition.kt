package kr.eme.plugin.cutscene.objects

data class Transition(
    val startSeq: Int,
    val endSeq: Int,
    val time: Long = 20L //기본값으로 20틱(1초)로 설정
)
