package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun SaveThemeDialog(
    initialName: String,
    showNameError: Boolean,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by rememberSaveable(initialName) { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.theme_builder_save_dialog_title),
                style = AppTheme.typography.h3,
                color = AppTheme.colors.text,
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = name,
                    onValueChange = { if (it.length <= 32) name = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.theme_builder_name_hint),
                            color = AppTheme.colors.textSecondary,
                        )
                    },
                    isError = showNameError && name.isBlank(),
                    singleLine = true,
                )
                if (showNameError && name.isBlank()) {
                    Text(
                        modifier = Modifier.padding(top = AppTheme.paddings.small),
                        text = stringResource(R.string.theme_builder_name_empty_error),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.error,
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name) }) {
                Text(
                    text = stringResource(R.string.theme_builder_save),
                    color = AppTheme.colors.primary,
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