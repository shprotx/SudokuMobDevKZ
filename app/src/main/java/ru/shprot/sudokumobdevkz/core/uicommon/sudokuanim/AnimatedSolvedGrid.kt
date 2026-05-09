package ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

private const val INITIAL_DELAY_MS = 400L
private const val CELL_DELAY_MS = 25L

@Composable
fun AnimatedSolvedGrid(
    modifier: Modifier,
) {
    var visibleCells by remember { mutableStateOf(SolvedPuzzleData.INITIAL_FILLED) }
    var animationKey by remember { mutableIntStateOf(0) }
    var isAnimating by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(animationKey) {
        isAnimating = true
        visibleCells = SolvedPuzzleData.INITIAL_FILLED

        val emptyCells = (0 until 9).flatMap { row ->
            (0 until 9).map { col -> GridPoint(row, col) }
        }.filter { it !in SolvedPuzzleData.INITIAL_FILLED }.shuffled()

        delay(INITIAL_DELAY_MS)

        for (index in emptyCells.indices) {
            visibleCells = SolvedPuzzleData.INITIAL_FILLED + emptyCells.take(index + 1).toSet()
            delay(CELL_DELAY_MS)
        }

        isAnimating = false
    }

    SolvedGridAnimation(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            enabled = !isAnimating,
        ) {
            animationKey++
        },
        initialFilled = SolvedPuzzleData.INITIAL_FILLED,
        solvedGrid = SolvedPuzzleData.SOLVED_GRID,
        visibleCells = visibleCells,
    )
}
