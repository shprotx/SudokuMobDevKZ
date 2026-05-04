package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface GameUIEffect : UIEffect {
    data object NavigateBack : GameUIEffect
    data object NavigateToSettings : GameUIEffect

    class NavigateToGameOver(
        val isWin: Boolean,
        val time: String,
        val errors: Int,
    ) : GameUIEffect

    class NavigateToNewGame(val difficultyOrdinal: Int) : GameUIEffect
}
