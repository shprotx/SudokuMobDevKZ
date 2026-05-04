package ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class GameOverUIState(
    val isWin: Boolean = false,
    val time: String = "00:00",
    val errors: Int = 0,
    val difficulty: Difficulty = Difficulty.EASY,
) : UIState
