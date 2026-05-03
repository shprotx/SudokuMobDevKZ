package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface GameUIEffect : UIEffect {
    data class NavigateToGameOver(
        val isWin: Boolean,
        val time: String,
        val errors: Int,
    ) : GameUIEffect

    data class NavigateToNewGame(val difficulty: Int) : GameUIEffect
    data object NavigateBack : GameUIEffect
}
