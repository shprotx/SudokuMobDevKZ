package ru.shprot.sudokumobdevkz.feature.menu.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class MenuUIState(
    val hasSavedGame: Boolean = false,
    val selectedDifficulty: Int = 0,
) : UIState
