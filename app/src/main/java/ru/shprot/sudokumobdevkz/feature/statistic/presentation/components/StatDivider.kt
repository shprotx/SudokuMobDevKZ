package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun StatDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
        thickness = AppTheme.sizes.dividerThickness,
        color = AppTheme.colors.divider,
    )
}
