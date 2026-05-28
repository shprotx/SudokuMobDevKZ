package ru.shprot.sudokumobdevkz.feature.feedback.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.components.FeedbackSendButton
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIEvent
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIState

private const val MAX_FEEDBACK_LENGTH = 4000

@Composable
fun FeedbackScreenContent(
    uiState: FeedbackUIState,
    onEvent: (FeedbackUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .verticalScroll(rememberScrollState()),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.feedback),
            onLeadIconClick = { onEvent(FeedbackUIEvent.BackClicked) },
        )

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.paddings.large)
                .navigationBarsPadding(),
        ) {
            Text(
                modifier = Modifier.padding(bottom = AppTheme.paddings.xxl),
                text = stringResource(R.string.feedback_description),
                style = AppTheme.typography.body2,
                color = AppTheme.colors.textSecondary,
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.text,
                onValueChange = { input ->
                    if (input.length <= MAX_FEEDBACK_LENGTH) {
                        onEvent(FeedbackUIEvent.TextChanged(input))
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.feedback_text_hint),
                        style = AppTheme.typography.body2,
                        color = AppTheme.colors.textSecondary,
                    )
                },
                minLines = 5,
                enabled = !uiState.isSending,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AppTheme.colors.primary,
                    unfocusedBorderColor = AppTheme.colors.divider,
                    focusedTextColor = AppTheme.colors.text,
                    unfocusedTextColor = AppTheme.colors.text,
                    cursorColor = AppTheme.colors.primary,
                ),
                textStyle = AppTheme.typography.body2,
            )

            FeedbackSendButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.xxl)
                    .padding(bottom = AppTheme.paddings.xxxl),
                isSending = uiState.isSending,
                enabled = uiState.text.isNotBlank() && !uiState.isSending,
                onClick = { onEvent(FeedbackUIEvent.SendClicked) },
            )
        }
    }
}
