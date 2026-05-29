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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeMode
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.ThemePalette
import ru.shprot.sudokumobdevkz.core.theme.ThemePalettes

private const val SWATCHES_PER_ROW = 5

@Composable
internal fun ThemeQuickPicker(
    selectedThemeId: String,
    onSelect: (String) -> Unit,
    onClose: () -> Unit,
) {
    Surface(
        modifier = Modifier.width(AppTheme.sizes.themePickerWidth),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        color = AppTheme.colors.backgroundCard,
        shadowElevation = AppTheme.sizes.elevationMedium,
    ) {
        Column(modifier = Modifier.padding(bottom = AppTheme.paddings.medium)) {

            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onClose,
                ) {
                    Icon(
                        modifier = Modifier.size(AppTheme.sizes.iconMedium),
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.go_back),
                        tint = AppTheme.colors.iconTint,
                    )
                }
            }

            ThemeRow(
                label = stringResource(R.string.theme_mode_system),
                isSelected = selectedThemeId == ThemeMode.System.id,
                onClick = { onSelect(ThemeMode.System.id) },
            )

            PickerDivider()

            ThemeRow(
                label = stringResource(R.string.theme_mode_light),
                isSelected = selectedThemeId == ThemeMode.Light.id,
                onClick = { onSelect(ThemeMode.Light.id) },
            )

            SwatchGrid(
                palettes = ThemePalettes.all.filterNot { it.isDark },
                selectedThemeId = selectedThemeId,
                onSelect = onSelect,
            )

            PickerDivider()

            ThemeRow(
                label = stringResource(R.string.theme_mode_dark),
                isSelected = selectedThemeId == ThemeMode.Dark.id,
                onClick = { onSelect(ThemeMode.Dark.id) },
            )

            SwatchGrid(
                palettes = ThemePalettes.all.filter { it.isDark },
                selectedThemeId = selectedThemeId,
                onSelect = onSelect,
            )
        }
    }
}

@Composable
internal fun ThemeRow(
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
internal fun SwatchGrid(
    palettes: List<ThemePalette>,
    selectedThemeId: String,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier = Modifier.padding(
            horizontal = AppTheme.paddings.large,
            vertical = AppTheme.paddings.small,
        ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
    ) {
        palettes.chunked(SWATCHES_PER_ROW).forEach { rowPalettes ->
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium)) {
                rowPalettes.forEach { palette ->
                    ThemeSwatch(
                        color = Color(palette.colors.primary),
                        isSelected = selectedThemeId == palette.id,
                        onClick = { onSelect(palette.id) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun ThemeSwatch(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall)
    Box(
        modifier = Modifier
            .size(AppTheme.sizes.colorSwatch)
            .clip(shape)
            .background(color, shape)
            .border(
                width = if (isSelected) AppTheme.sizes.elevationSmall else AppTheme.sizes.dividerThickness,
                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.divider,
                shape = shape,
            )
            .clickable(onClick = onClick),
    )
}

@Composable
internal fun PickerDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.small)
            .height(AppTheme.sizes.dividerThickness)
            .background(AppTheme.colors.divider),
    )
}
