package ru.shprot.sudokumobdevkz.feature.splash.presentation.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.delay
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

private val SOLVED_GRID = arrayOf(
    intArrayOf(5, 3, 4, 6, 7, 8, 9, 1, 2),
    intArrayOf(6, 7, 2, 1, 9, 5, 3, 4, 8),
    intArrayOf(1, 9, 8, 3, 4, 2, 5, 6, 7),
    intArrayOf(8, 5, 9, 7, 6, 1, 4, 2, 3),
    intArrayOf(4, 2, 6, 8, 5, 3, 7, 9, 1),
    intArrayOf(7, 1, 3, 9, 2, 4, 8, 5, 6),
    intArrayOf(9, 6, 1, 5, 3, 7, 2, 8, 4),
    intArrayOf(2, 8, 7, 4, 1, 9, 6, 3, 5),
    intArrayOf(3, 4, 5, 2, 8, 6, 1, 7, 9),
)

private val INITIAL_FILLED = setOf(
    0 to 0, 0 to 1, 0 to 4, 0 to 8,
    1 to 2, 1 to 5, 1 to 7,
    2 to 0, 2 to 3, 2 to 6,
    3 to 1, 3 to 4, 3 to 8,
    4 to 0, 4 to 3, 4 to 5, 4 to 8,
    5 to 0, 5 to 4, 5 to 7,
    6 to 2, 6 to 5, 6 to 8,
    7 to 1, 7 to 3, 7 to 6,
    8 to 0, 8 to 4, 8 to 7, 8 to 8,
)

@Composable
fun SplashScreen(onNavigateToMenu: () -> Unit) {
    val emptyCells = remember {
        val cells = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                if ((row to col) !in INITIAL_FILLED) {
                    cells.add(row to col)
                }
            }
        }
        cells.shuffled()
    }

    var filledCount by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        delay(400)
        for (i in emptyCells.indices) {
            filledCount = i + 1
            delay(25)
        }
        delay(300)
        onNavigateToMenu()
    }

    val visibleCells = remember(filledCount) {
        INITIAL_FILLED + emptyCells.take(filledCount).toSet()
    }

    val gridLine = AppTheme.colors.gridLine
    val gridLineBold = AppTheme.colors.gridLineBold
    val background = AppTheme.colors.backgroundCard
    val fixedColor = AppTheme.colors.cellFixed
    val fillingColor = AppTheme.colors.primary
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.paddings.large),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Sudoku",
                style = AppTheme.typography.h1,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = "v${ru.shprot.sudokumobdevkz.BuildConfig.VERSION_NAME}",
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )

            Canvas(
                modifier = Modifier
                    .padding(top = AppTheme.paddings.xxl)
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f),
            ) {
                val cellSize = size.width / 9f

                drawRect(color = background, size = size)

                drawSplashNumbers(
                    cellSize = cellSize,
                    visibleCells = visibleCells,
                    initialFilled = INITIAL_FILLED,
                    fixedColor = fixedColor,
                    fillingColor = fillingColor,
                    textMeasurer = textMeasurer,
                    density = density,
                )

                drawSplashGridLines(cellSize, gridLine, gridLineBold)
            }
        }
    }
}

private fun DrawScope.drawSplashGridLines(
    cellSize: Float,
    thinColor: Color,
    boldColor: Color,
) {
    for (i in 0..9) {
        val pos = i * cellSize
        val isBold = i % 3 == 0
        val color = if (isBold) boldColor else thinColor
        val width = if (isBold) 2f else 0.5f

        drawLine(color, Offset(pos, 0f), Offset(pos, size.height), width)

        drawLine(color, Offset(0f, pos), Offset(size.width, pos), width)
    }
}

private fun DrawScope.drawSplashNumbers(
    cellSize: Float,
    visibleCells: Set<Pair<Int, Int>>,
    initialFilled: Set<Pair<Int, Int>>,
    fixedColor: Color,
    fillingColor: Color,
    textMeasurer: TextMeasurer,
    density: Density,
) {
    val fontSizeSp = with(density) { (cellSize * 0.42f).toSp() }

    for ((row, col) in visibleCells) {
        val num = SOLVED_GRID[row][col]
        val isInitial = (row to col) in initialFilled
        val color = if (isInitial) fixedColor else fillingColor

        val style = TextStyle(
            fontSize = fontSizeSp,
            fontWeight = if (isInitial) FontWeight.Medium else FontWeight.Normal,
            color = color,
        )

        val text = num.toString()
        val measured = textMeasurer.measure(text, style)
        val x = col * cellSize + (cellSize - measured.size.width) / 2f
        val y = row * cellSize + (cellSize - measured.size.height) / 2f
        drawText(measured, topLeft = Offset(x, y))
    }
}
