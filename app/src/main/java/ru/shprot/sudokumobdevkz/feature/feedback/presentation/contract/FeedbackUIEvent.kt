package ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIEvent

sealed interface FeedbackUIEvent : UIEvent {
    data object BackClicked : FeedbackUIEvent
    data object SendClicked : FeedbackUIEvent

    data class TextChanged(val text: String) : FeedbackUIEvent
}