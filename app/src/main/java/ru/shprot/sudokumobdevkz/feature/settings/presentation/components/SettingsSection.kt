package ru.shprot.sudokumobdevkz.feature.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
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
fun SettingsSectionHeader(
    modifier: Modifier = Modifier,
    title: String,
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = AppTheme.paddings.xxl,
                bottom = AppTheme.paddings.default,
            ),
        text = title,
        style = AppTheme.typography.h4,
        color = AppTheme.colors.text,
    )
}

@Composable
fun SettingsToggleItem(
    modifier: Modifier = Modifier,
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
                checkedTrackColor = AppTheme.colors.primary,
            ),
        )
    }
}

@Composable
fun SettingsNavItem(
    modifier: Modifier = Modifier,
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

@Composable
fun SettingsDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier,
        thickness = AppTheme.sizes.dividerThickness,
        color = AppTheme.colors.divider,
    )
}
