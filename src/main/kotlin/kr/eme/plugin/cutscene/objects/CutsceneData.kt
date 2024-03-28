package kr.eme.plugin.cutscene.objects

import org.bukkit.Location

data class CutsceneData(
    val name: String,
    private val sequences: MutableMap<Int, Location> = mutableMapOf(),
    val transitions: MutableList<Transition> = mutableListOf()
) {
    fun addSequence(seq: Int, location: Location) {
        sequences[seq] = location
    }
    fun getSequenceLocation(seq: Int): Location? = sequences[seq]
    fun addTransition(transition: Transition) {
        transitions.add(transition)
    }
}
