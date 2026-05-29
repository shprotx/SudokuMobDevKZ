package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.domain.model.toAppColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid

@Composable
internal fun MiniThemePreview(
    colors: ThemeColors,
    modifier: Modifier = Modifier,
) {
    val appColors = colors.toAppColors()

    SudokuTheme(colors = appColors) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                .background(appColors.background)
                .padding(AppTheme.paddings.default),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SudokuGrid(
                modifier = Modifier.width(AppTheme.sizes.colorPickerPreview),
                cells = SampleSudoku.cells(),
                selectedRow = SampleSudoku.SELECTED_ROW,
                selectedCol = SampleSudoku.SELECTED_COL,
                highlightedNumber = SampleSudoku.HIGHLIGHTED_NUMBER,
                onCellClick = { _, _ -> },
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.small),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall))
                        .background(appColors.primary)
                        .padding(vertical = AppTheme.paddings.small),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.theme_builder_color_primary),
                        style = AppTheme.typography.body3,
                        color = appColors.textOnPrimary,
                    )
                }

                Text(
                    text = stringResource(R.string.theme_builder_color_text),
                    style = AppTheme.typography.body2,
                    color = appColors.text,
                )

                Text(
                    text = stringResource(R.string.theme_builder_color_text_secondary),
                    style = AppTheme.typography.body3,
                    color = appColors.textSecondary,
                )
            }
        }
    }
}
