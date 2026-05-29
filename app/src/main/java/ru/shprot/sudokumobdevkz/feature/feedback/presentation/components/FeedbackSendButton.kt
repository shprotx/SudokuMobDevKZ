package ru.shprot.sudokumobdevkz.feature.feedback.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun FeedbackSendButton(
    modifier: Modifier,
    isSending: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.height(AppTheme.sizes.buttonHeight),
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppTheme.colors.primary,
            contentColor = AppTheme.colors.textOnPrimary,
            disabledContainerColor = AppTheme.colors.primary.copy(alpha = 0.4f),
            disabledContentColor = AppTheme.colors.textOnPrimary.copy(alpha = 0.6f),
        ),
    ) {
        if (isSending) {
            CircularProgressIndicator(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                color = AppTheme.colors.textOnPrimary,
                strokeWidth = AppTheme.sizes.dividerThickness * 2,
            )
        } else {
            Text(
                text = stringResource(R.string.feedback_send),
                style = AppTheme.typography.button,
            )
        }
    }
}
