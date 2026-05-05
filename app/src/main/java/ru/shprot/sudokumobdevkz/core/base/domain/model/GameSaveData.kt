package ru.shprot.sudokumobdevkz.core.base.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GameSaveData(
    val difficulty: Int,
    val timeSeconds: Int,
    val errors: Int,
    val maxErrors: Int,
    val hintsRemaining: Int,
    val isNotesEnabled: Boolean,
    val cells: List<List<CellSave>>,
    val solution: List<List<Int>>,
    val isStandardMode: Boolean = true,
) {
    @Serializable
    data class CellSave(
        val value: Int = 0,
        val isGiven: Boolean = false,
        val isError: Boolean = false,
        val notes: Set<Int> = emptySet(),
    )
}
