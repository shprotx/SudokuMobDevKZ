package ru.shprot.sudokumobdevkz.feature.settings.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonText

@Composable
fun SettingsLockedDialog(
    onDismiss: () -> Unit,
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.settings),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.settings_locked_during_game),
            )
        },
        confirmButton = {
            ButtonText(
                modifier = Modifier,
                text = stringResource(R.string.ok),
                onClick = onDismiss,
            )
        },
    )
}
