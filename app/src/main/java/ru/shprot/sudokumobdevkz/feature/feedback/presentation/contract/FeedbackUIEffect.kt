package ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEffect

sealed interface FeedbackUIEffect : UIEffect {
    data object NavigateBack : FeedbackUIEffect
}