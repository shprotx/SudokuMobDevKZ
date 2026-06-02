package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

data class DayCellUiModel(
    val dateKey: String,
    val dayNumber: Int,
    val state: DayCellState,
)