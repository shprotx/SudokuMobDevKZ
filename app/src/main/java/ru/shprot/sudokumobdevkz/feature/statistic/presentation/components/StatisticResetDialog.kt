package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

@Composable
fun StatisticResetDialog(
    difficultyName: String,
    showCloudWarning: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.reset_statistics_title)
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(
                        R.string.reset_statistics_diff_confirm,
                        difficultyName,
                    ),
                )

                if (showCloudWarning) {
                    Text(
                        modifier = Modifier.padding(top = AppTheme.paddings.default),
                        text = stringResource(R.string.reset_statistics_cloud_warning),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        },
        confirmButton = {
            ButtonText(
                modifier = Modifier,
                text = stringResource(R.string.reset),
                textColor = AppTheme.colors.error,
                onClick = onConfirm,
            )
        },
        dismissButton = {
            ButtonText(
                modifier = Modifier,
                text = stringResource(R.string.cancel),
                onClick = onDismiss,
            )
        },
        containerColor = AppTheme.colors.backgroundCard,
        titleContentColor = AppTheme.colors.text,
        textContentColor = AppTheme.colors.textSecondary,
    )
}
