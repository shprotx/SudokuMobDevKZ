package ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState
import ru.shprot.sudokumobdevkz.core.base.util.empty

data class FeedbackUIState(
    val text: String = String.empty,
    val isSending: Boolean = false,
) : UIState