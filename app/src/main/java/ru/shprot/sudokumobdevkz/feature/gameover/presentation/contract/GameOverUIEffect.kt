package ru.shprot.sudokumobdevkz.feature.gameover.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface GameOverUIEffect : UIEffect {
    data object NavigateToMenu : GameOverUIEffect

    class NavigateToNewGame(val difficulty: Int) : GameOverUIEffect
}
