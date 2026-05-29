package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
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
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameActionsBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameStatusBar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.GameToolbar
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.NumberPanel
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.SudokuGrid

@Composable
internal fun ThemePreviewBlock(
    colors: ThemeColors,
    modifier: Modifier = Modifier,
) {
    val appColors = colors.toAppColors()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .consumeWindowInsets(WindowInsets.statusBars),
    ) {
        SudokuTheme(colors = appColors) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appColors.background)
                    .padding(bottom = AppTheme.paddings.default),
            ) {
                GameToolbar(
                    modifier = Modifier,
                    onBackClick = {},
                    onRestartClick = {},
                    onPauseClick = {},
                    onSettingsClick = {},
                )

                Spacer(modifier = Modifier.height(AppTheme.paddings.medium))

                GameStatusBar(
                    modifier = Modifier,
                    difficultyLabel = stringResource(R.string.difficulty_middle),
                    errors = PREVIEW_ERRORS,
                    maxErrors = PREVIEW_MAX_ERRORS,
                    lives = PREVIEW_MAX_ERRORS - PREVIEW_ERRORS,
                    timer = PREVIEW_TIMER,
                )

                SudokuGrid(
                    modifier = Modifier.padding(
                        top = AppTheme.paddings.default,
                        start = AppTheme.paddings.medium,
                        end = AppTheme.paddings.medium,
                    ),
                    cells = SampleSudoku.cells(),
                    selectedRow = SampleSudoku.SELECTED_ROW,
                    selectedCol = SampleSudoku.SELECTED_COL,
                    highlightedNumber = SampleSudoku.HIGHLIGHTED_NUMBER,
                    onCellClick = { _, _ -> },
                )

                Spacer(modifier = Modifier.height(AppTheme.paddings.large))

                NumberPanel(
                    modifier = Modifier.padding(horizontal = AppTheme.paddings.medium),
                    availableNumbers = PREVIEW_AVAILABLE_NUMBERS,
                    isNotesMode = false,
                    onNumberClick = {},
                )

                Spacer(modifier = Modifier.height(AppTheme.paddings.large))

                GameActionsBar(
                    modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
                    isNotesEnabled = false,
                    hintsRemaining = PREVIEW_HINTS,
                    onUndoClick = {},
                    onEraseClick = {},
                    onNotesClick = {},
                    onHintClick = {},
                )
            }
        }
    }
}

private const val PREVIEW_ERRORS = 1
private const val PREVIEW_MAX_ERRORS = 3
private const val PREVIEW_TIMER = "02:34"
private const val PREVIEW_HINTS = 3
private val PREVIEW_AVAILABLE_NUMBERS = (1..9).toSet()
