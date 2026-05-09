package ru.shprot.sudokumobdevkz.feature.splash.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim.GridPoint

data class SplashUIState(
    val visibleCells: Set<GridPoint> = emptySet(),
) : UIState
