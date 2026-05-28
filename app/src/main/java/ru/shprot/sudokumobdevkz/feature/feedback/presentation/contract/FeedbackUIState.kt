package ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class FeedbackUIState(
    val text: String = "",
    val isSending: Boolean = false,
) : UIState