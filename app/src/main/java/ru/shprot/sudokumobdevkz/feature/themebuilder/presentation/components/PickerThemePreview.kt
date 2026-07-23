package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors
import ru.shprot.sudokumobdevkz.core.base.domain.model.toAppColors
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.theme.SudokuTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid

@Composable
internal fun PickerThemePreview(
    colors: ThemeColors,
    modifier: Modifier = Modifier,
) {
    val appColors = colors.toAppColors()

    SudokuTheme(colors = appColors) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                .background(appColors.background)
                .padding(AppTheme.paddings.default),
        ) {
            GameStatusBar(
                modifier = Modifier,
                difficultyLabel = stringResource(R.string.difficulty_middle),
                errors = PREVIEW_ERRORS,
                maxErrors = PREVIEW_MAX_ERRORS,
                lives = PREVIEW_MAX_ERRORS - PREVIEW_ERRORS,
                timer = PREVIEW_TIMER,
            )

            SudokuGrid(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                cells = SampleSudoku.cells(),
                selectedRow = SampleSudoku.SELECTED_ROW,
                selectedCol = SampleSudoku.SELECTED_COL,
                highlightedNumber = SampleSudoku.HIGHLIGHTED_NUMBER,
                onCellClick = { _, _ -> },
            )

            NumberPanel(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                availableNumbers = PREVIEW_AVAILABLE_NUMBERS,
                isNotesMode = false,
                onNumberClick = {},
            )
        }
    }
}

private const val PREVIEW_ERRORS = 1
private const val PREVIEW_MAX_ERRORS = 3
private const val PREVIEW_TIMER = "02:34"
private val PREVIEW_AVAILABLE_NUMBERS = (1..9).toSet()