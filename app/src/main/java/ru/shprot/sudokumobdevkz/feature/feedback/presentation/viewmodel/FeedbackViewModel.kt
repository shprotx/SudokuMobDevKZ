package ru.shprot.sudokumobdevkz.feature.feedback.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.core.base.util.empty
import ru.shprot.sudokumobdevkz.feature.feedback.domain.usecase.SendFeedbackUseCase
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIEffect
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIEvent
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIState
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val sendFeedback: SendFeedbackUseCase,
) : BaseViewModel<FeedbackUIEvent, FeedbackUIState, FeedbackUIEffect>(FeedbackUIState()) {

    override fun handleUIEvent(event: FeedbackUIEvent) = when (event) {
        FeedbackUIEvent.BackClicked ->
            setEffect(FeedbackUIEffect.NavigateBack)

        FeedbackUIEvent.SendClicked ->
            handleSend()

        is FeedbackUIEvent.TextChanged ->
            updateState { copy(text = event.text) }
    }

    private fun handleSend() {
        if (!sendFeedback.isConfigured) {
            if (BuildConfig.DEBUG) {
                Log.d("FeedbackViewModel", "Feedback API URL not configured")
            }
            SnackbarManager.show(R.string.feedback_unavailable)
            return
        }
        updateState { copy(isSending = true) }
        viewModelScope.launch(exceptionHandler) {
            val result = sendFeedback.execute(currentState.text)
            result.fold(
                onSuccess = {
                    SnackbarManager.show(R.string.feedback_sent_success)
                    updateState { copy(text = String.empty, isSending = false) }
                    delay(1500)
                    setEffect(FeedbackUIEffect.NavigateBack)
                },
                onFailure = {
                    SnackbarManager.show(R.string.feedback_send_failed, isError = true)
                    updateState { copy(isSending = false) }
                },
            )
        }
    }
}