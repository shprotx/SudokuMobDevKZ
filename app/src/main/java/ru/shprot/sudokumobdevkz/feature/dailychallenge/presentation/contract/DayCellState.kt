package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty

sealed interface DayCellState {
    data class Completed(val difficulty: Difficulty) : DayCellState
    data object Today : DayCellState
    data object Missed : DayCellState
    data object Future : DayCellState
}