package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ToolbarPillButton
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.core.uicommon.glass.glassBar

@Composable
fun GameToolbar(
    modifier: Modifier,
    themePopupExpanded: Boolean,
    selectedThemeId: String,
    isLayoutEditMode: Boolean,
    onBackClick: () -> Unit,
    onRestartClick: () -> Unit,
    onPauseClick: () -> Unit,
    onLayoutEditClick: () -> Unit,
    onPaletteClick: () -> Unit,
    onThemeSelected: (String) -> Unit,
    onDismissThemePopup: () -> Unit,
    onSettingsClick: () -> Unit,
) {

    val barShape = RoundedCornerShape(AppTheme.sizes.cornerRadiusFull)

    Row(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            )
            .glassBar(shape = barShape)
            .padding(
                horizontal = AppTheme.paddings.medium,
                vertical = AppTheme.paddings.small,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {

        ToolbarPillButton(
            modifier = Modifier,
            icon = AppIcons.Back,
            contentDescription = stringResource(R.string.go_back),
            onClick = onBackClick,
        )

        ToolbarPillButton(
            modifier = Modifier,
            icon = AppIcons.Restart,
            contentDescription = stringResource(R.string.restart),
            onClick = onRestartClick,
        )

        ToolbarPillButton(
            modifier = Modifier,
            icon = AppIcons.Pause,
            contentDescription = stringResource(R.string.pause),
            onClick = onPauseClick,
        )

        ToolbarPillButton(
            modifier = Modifier,
            icon = when (isLayoutEditMode) {
                true -> AppIcons.Check
                false -> AppIcons.Layout
            },
            contentDescription = stringResource(R.string.layout_edit_title),
            isHighlighted = isLayoutEditMode,
            onClick = onLayoutEditClick,
        )

        Box {
            ToolbarPillButton(
                modifier = Modifier,
                icon = AppIcons.Palette,
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

        ToolbarPillButton(
            modifier = Modifier,
            icon = AppIcons.Settings,
            contentDescription = stringResource(R.string.settings),
            onClick = onSettingsClick,
        )
    }
}