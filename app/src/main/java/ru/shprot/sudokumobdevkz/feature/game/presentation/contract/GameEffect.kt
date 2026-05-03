package ru.shprot.sudokumobdevkz.feature.game.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface GameEffect : UIEffect {
    data class NavigateToGameOver(
        val isWin: Boolean,
        val time: String,
        val errors: Int,
    ) : GameEffect
}
