package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.ThemePalettes

@Composable
internal fun PresetChipRow(
    modifier: Modifier = Modifier,
    onPresetSelected: (ThemeColors) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = AppTheme.paddings.large),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {
        items(ThemePalettes.all) { preset ->
            PresetChip(
                label = stringResource(preset.labelRes),
                onClick = { onPresetSelected(preset.colors) },
            )
        }
    }
}

@Composable
internal fun PresetChip(
    label: String,
    onClick: () -> Unit,
) {
    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusFull))
            .background(AppTheme.colors.chipUnselected)
            .clickable(onClick = onClick)
            .padding(
                horizontal = AppTheme.paddings.large,
                vertical = AppTheme.paddings.medium,
            ),
        text = label,
        style = AppTheme.typography.body3,
        color = AppTheme.colors.chipTextUnselected,
    )
}
