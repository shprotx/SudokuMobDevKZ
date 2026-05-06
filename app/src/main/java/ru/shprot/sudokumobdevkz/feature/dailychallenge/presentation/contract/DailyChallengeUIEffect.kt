package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface DailyChallengeUIEffect : UIEffect {
    data object NavigateBack : DailyChallengeUIEffect

    class NavigateToGame(val difficultyOrdinal: Int) : DailyChallengeUIEffect
}