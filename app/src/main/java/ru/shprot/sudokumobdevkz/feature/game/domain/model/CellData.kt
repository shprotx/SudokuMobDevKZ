package ru.shprot.sudokumobdevkz.feature.game.domain.model

data class CellData(
    val value: Int = 0,
    val isGiven: Boolean = false,
    val isError: Boolean = false,
    val notes: Set<Int> = emptySet(),
)
