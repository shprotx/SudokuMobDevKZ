package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ResultStat(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(AppTheme.sizes.iconSmall),
            imageVector = icon,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary,
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.small),
            text = label,
            style = AppTheme.typography.caption1,
            color = AppTheme.colors.textSecondary,
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.small),
            text = value,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.text,
        )
    }
}