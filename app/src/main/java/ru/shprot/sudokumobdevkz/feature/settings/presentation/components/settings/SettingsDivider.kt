package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsDivider(modifier: Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = AppTheme.sizes.dividerThickness,
        color = AppTheme.colors.divider,
    )
}
