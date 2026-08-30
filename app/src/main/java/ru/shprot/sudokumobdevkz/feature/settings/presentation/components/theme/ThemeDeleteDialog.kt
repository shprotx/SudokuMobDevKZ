package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.theme

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ThemeDeleteDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.theme_delete_confirm_title),
                style = AppTheme.typography.h3,
                color = AppTheme.colors.text,
            )
        },
        text = {
            Text(
                text = stringResource(R.string.theme_delete_confirm_message),
                style = AppTheme.typography.body1,
                color = AppTheme.colors.textSecondary,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.theme_list_delete),
                    color = AppTheme.colors.error,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = AppTheme.colors.textSecondary,
                )
            }
        },
        containerColor = AppTheme.colors.backgroundCard,
    )
}