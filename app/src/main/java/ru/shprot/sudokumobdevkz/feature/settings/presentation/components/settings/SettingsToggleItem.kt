package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SettingsToggleItem(
    modifier: Modifier,
    icon: ImageVector,
    iconTint: Color = AppTheme.colors.primary,
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (enabled) iconTint else AppTheme.colors.textSecondary,
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = AppTheme.paddings.default),
            text = title,
            style = AppTheme.typography.body1,
            color = if (enabled) AppTheme.colors.text else AppTheme.colors.textSecondary,
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = AppTheme.colors.textOnPrimary,
                checkedTrackColor = AppTheme.colors.primary,
                checkedBorderColor = AppTheme.colors.primary,
                uncheckedThumbColor = AppTheme.colors.backgroundCard,
                uncheckedTrackColor = AppTheme.colors.divider,
                uncheckedBorderColor = AppTheme.colors.divider,
                disabledCheckedThumbColor = AppTheme.colors.textOnPrimary.copy(alpha = 0.38f),
                disabledCheckedTrackColor = AppTheme.colors.primary.copy(alpha = 0.12f),
                disabledCheckedBorderColor = AppTheme.colors.primary.copy(alpha = 0.12f),
                disabledUncheckedThumbColor = AppTheme.colors.textSecondary.copy(alpha = 0.38f),
                disabledUncheckedTrackColor = AppTheme.colors.divider.copy(alpha = 0.12f),
                disabledUncheckedBorderColor = AppTheme.colors.divider.copy(alpha = 0.12f),
            ),
        )
    }
}
