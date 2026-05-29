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
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData
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
                    cells = previewCells(),
                    selectedRow = PREVIEW_SELECTED_ROW,
                    selectedCol = PREVIEW_SELECTED_COL,
                    highlightedNumber = PREVIEW_HIGHLIGHTED_NUMBER,
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
private const val PREVIEW_SELECTED_ROW = 4
private const val PREVIEW_SELECTED_COL = 2
private const val PREVIEW_HIGHLIGHTED_NUMBER = 8
private const val PREVIEW_HINTS = 3
private val PREVIEW_AVAILABLE_NUMBERS = (1..9).toSet()

private fun previewCells(): List<List<CellData>> {
    fun given(value: Int) = CellData(value = value, isGiven = true)
    fun user(value: Int) = CellData(value = value)
    fun error(value: Int) = CellData(value = value, isError = true)
    fun notes(vararg n: Int) = CellData(notes = n.toSet())
    val empty = CellData()

    return listOf(
        listOf(given(5), given(3), user(4), empty, given(7), empty, empty, empty, given(8)),
        listOf(given(6), empty, notes(1, 2, 4), given(1), given(9), given(5), empty, empty, empty),
        listOf(empty, given(9), given(8), empty, empty, empty, empty, given(6), empty),
        listOf(given(8), empty, empty, empty, given(6), empty, empty, empty, given(3)),
        listOf(given(4), empty, empty, given(8), empty, given(3), notes(2, 5, 9), empty, given(1)),
        listOf(given(7), error(1), empty, empty, given(2), empty, empty, empty, given(6)),
        listOf(empty, given(6), empty, empty, empty, empty, given(2), given(8), empty),
        listOf(empty, empty, empty, given(4), given(1), given(9), empty, empty, given(5)),
        listOf(empty, empty, empty, empty, given(8), empty, empty, given(7), given(9)),
    )
}
