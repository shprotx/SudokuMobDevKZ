package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.model.ThemeColorKey

@Composable
internal fun ColorCategoryList(
    colors: ThemeColors,
    onColorKeyClick: (ThemeColorKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.colors.backgroundCard, RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge))
            .padding(horizontal = AppTheme.paddings.large, vertical = AppTheme.paddings.medium),
    ) {
        ThemeColorKey.entries.forEach { key ->
            ColorRow(
                label = stringResource(key.labelRes),
                colorValue = key.get(colors),
                onClick = { onColorKeyClick(key) },
            )
        }
    }
}
