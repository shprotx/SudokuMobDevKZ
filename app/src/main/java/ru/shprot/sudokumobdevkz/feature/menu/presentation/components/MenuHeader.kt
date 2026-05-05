package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ToolbarCircleButton

@Composable
fun MenuHeader(
    modifier: Modifier,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = stringResource(R.string.app_name),
                style = AppTheme.typography.h1,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = stringResource(R.string.subtitle_slogan),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }

        ToolbarCircleButton(
            modifier = Modifier,
            icon = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings),
            onClick = onSettingsClick,
        )
    }
}
