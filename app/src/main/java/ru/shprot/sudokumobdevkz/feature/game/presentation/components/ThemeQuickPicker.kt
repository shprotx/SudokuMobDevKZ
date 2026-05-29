package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.ThemePalettes

private const val SWATCHES_PER_ROW = 5

@Composable
internal fun ThemeQuickPicker(
    selectedThemeId: String,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(AppTheme.colors.backgroundCard)
            .width(AppTheme.sizes.themePickerWidth)
            .padding(vertical = AppTheme.paddings.small),
    ) {
        ThemeMode.builtIn().forEach { mode ->
            BuiltInThemeRow(
                label = builtInLabel(mode),
                isSelected = selectedThemeId == mode.id,
                onClick = { onSelect(mode.id) },
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.small)
                .height(AppTheme.sizes.dividerThickness)
                .background(AppTheme.colors.divider),
        )

        ThemePalettes.all.chunked(SWATCHES_PER_ROW).forEach { rowPalettes ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.small),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
            ) {
                rowPalettes.forEach { palette ->
                    ThemeSwatch(
                        background = Color(palette.colors.background),
                        primary = Color(palette.colors.primary),
                        isSelected = selectedThemeId == palette.id,
                        onClick = { onSelect(palette.id) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun BuiltInThemeRow(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.default),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = AppTheme.typography.body1,
            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.text,
        )

        if (isSelected) {
            Icon(
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = AppTheme.colors.primary,
            )
        }
    }
}

@Composable
internal fun ThemeSwatch(
    background: Color,
    primary: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(AppTheme.sizes.colorSwatch)
            .background(background, CircleShape)
            .border(
                width = if (isSelected) AppTheme.sizes.elevationSmall else AppTheme.sizes.dividerThickness,
                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.divider,
                shape = CircleShape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .size(AppTheme.sizes.colorSwatch / 2)
                .background(primary, CircleShape),
        )
    }
}

@Composable
internal fun builtInLabel(mode: ThemeMode): String = when (mode) {
    ThemeMode.System -> stringResource(R.string.theme_mode_system)
    ThemeMode.Light -> stringResource(R.string.theme_mode_light)
    ThemeMode.Dark -> stringResource(R.string.theme_mode_dark)
    is ThemeMode.Custom -> mode.title
}
