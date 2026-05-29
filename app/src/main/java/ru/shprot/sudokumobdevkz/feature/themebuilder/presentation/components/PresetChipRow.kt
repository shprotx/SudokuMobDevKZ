package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.BuiltInTheme

@Composable
internal fun PresetChipRow(
    modifier: Modifier = Modifier,
    onPresetSelected: (ThemeColors) -> Unit,
) {
    val presets = remember {
        listOf(
            BuiltInTheme.LIGHT.colors to R.string.theme_builder_preset_light,
            BuiltInTheme.DARK.colors to R.string.theme_builder_preset_dark,
            BuiltInTheme.SOLARIZED.colors to R.string.theme_builder_preset_solarized,
            BuiltInTheme.FOREST.colors to R.string.theme_builder_preset_forest,
        )
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = AppTheme.paddings.large),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
    ) {
        items(presets) { (colors, labelRes) ->
            FilterChip(
                selected = false,
                onClick = { onPresetSelected(colors) },
                label = { Text(text = stringResource(labelRes), style = AppTheme.typography.body3) },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = AppTheme.colors.chipUnselected,
                    labelColor = AppTheme.colors.chipTextUnselected,
                ),
            )
        }
    }
}