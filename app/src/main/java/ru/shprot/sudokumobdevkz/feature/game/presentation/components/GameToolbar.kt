package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ToolbarCircleButton

@Composable
fun GameToolbar(
    modifier: Modifier,
    themePopupExpanded: Boolean,
    selectedThemeId: String,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onPaletteClick: () -> Unit,
    onThemeSelected: (String) -> Unit,
    onDismissThemePopup: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
    ) {

        ToolbarCircleButton(
            modifier = Modifier,
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.go_back),
            onClick = onBackClick,
        )

        Text(
            modifier = Modifier
                .weight(1f),
            text = stringResource(R.string.app_name),
            style = AppTheme.typography.h3,
            color = AppTheme.colors.text,
        )

        ToolbarCircleButton(
            modifier = Modifier,
            icon = Icons.Filled.Refresh,
            contentDescription = stringResource(R.string.restart),
            onClick = onRestartClick,
        )

        ToolbarCircleButton(
            modifier = Modifier,
            icon = Icons.Filled.Pause,
            contentDescription = stringResource(R.string.pause),
            onClick = onPauseClick,
        )

        Box {
            ToolbarCircleButton(
                modifier = Modifier,
                icon = Icons.Filled.Palette,
                contentDescription = stringResource(R.string.theme_label),
                onClick = onPaletteClick,
            )

            if (themePopupExpanded) {
                Popup(
                    alignment = Alignment.TopEnd,
                    offset = IntOffset(x = 0, y = 0),
                    onDismissRequest = onDismissThemePopup,
                    properties = PopupProperties(focusable = true),
                ) {
                    ThemeQuickPicker(
                        selectedThemeId = selectedThemeId,
                        onSelect = onThemeSelected,
                        onClose = onDismissThemePopup,
                    )
                }
            }
        }

        ToolbarCircleButton(
            modifier = Modifier,
            icon = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings),
            onClick = onSettingsClick,
        )
    }
}
