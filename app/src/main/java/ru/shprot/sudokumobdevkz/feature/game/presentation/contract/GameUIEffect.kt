package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import androidx.annotation.StringRes
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface GameUIEffect : UIEffect {
    data object NavigateBack : GameUIEffect
    data object NavigateToSettings : GameUIEffect

    class NavigateToGameOver(
        val isWin: Boolean,
        val time: String,
        val errors: Int,
        val isDailyChallenge: Boolean = false,
        val newStreak: Int = 0,
    ) : GameUIEffect

    class NavigateToNewGame(val difficultyOrdinal: Int) : GameUIEffect
    class ShowMessage(@StringRes val messageRes: Int) : GameUIEffect
}