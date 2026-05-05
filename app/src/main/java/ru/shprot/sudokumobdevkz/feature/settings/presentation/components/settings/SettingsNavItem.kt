package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsNavItem(
    modifier: Modifier,
    icon: ImageVector,
    iconTint: Color = AppTheme.colors.iconTint,
    title: String,
    value: String = "",
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
            text = title,
            style = AppTheme.typography.body1,
            color = AppTheme.colors.text,
        )

        if (value.isNotEmpty()) {
            Text(
                text = value,
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }
    }
}
