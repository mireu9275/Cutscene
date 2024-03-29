package kr.eme.plugin.cutscene.objects

import org.bukkit.Location

data class CutsceneData(
    val name: String,
    private val sequences: MutableMap<Int, Location> = mutableMapOf(),
    private val transitions: MutableMap<Int, Transition> = mutableMapOf()
) {
    fun addSequence(seq: Int, location: Location) {
        sequences[seq] = location
    }
    fun getSequenceLocation(seq: Int): Location? = sequences[seq]
    fun getSequenceCount(): Int = sequences.size
    fun getSequenceLocations(): Map<Int, Location> = sequences.toMap()

    fun addTransition(index: Int, transition: Transition) {
        transitions[index] = transition
    }
    fun getTransition(index: Int): Transition? = transitions[index]
    fun getTransitions(): Map<Int, Transition> = transitions.toMap()
}