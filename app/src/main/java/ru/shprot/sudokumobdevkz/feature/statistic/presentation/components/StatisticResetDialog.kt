package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

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
            Text(
                text = stringResource(
                    R.string.reset_statistics_diff_confirm,
                    difficultyName
                )
            )
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
    )
}
