package ru.shprot.sudokumobdevkz.feature.splash.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.splash.domain.model.GridPoint

@Composable
fun SolvedGridAnimation(
    modifier: Modifier,
    initialFilled: Set<GridPoint>,
    solvedGrid: Array<IntArray>,
    visibleCells: Set<GridPoint>,
) {

    val gridLine = AppTheme.colors.gridLine
    val gridLineBold = AppTheme.colors.gridLineBold
    val background = AppTheme.colors.surface
    val fixedColor = AppTheme.colors.cellFixed
    val fillingColor = AppTheme.colors.primary
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    Canvas(
        modifier = modifier,
    ) {

        val cellSize = size.width / 9f

        drawRect(
            color = background,
            size = size,
        )

        drawSplashNumbers(
            cellSize = cellSize,
            visibleCells = visibleCells,
            initialFilled = initialFilled,
            solvedGrid = solvedGrid,
            fixedColor = fixedColor,
            fillingColor = fillingColor,
            textMeasurer = textMeasurer,
            density = density,
        )

        drawSplashGridLines(
            cellSize = cellSize,
            thinColor = gridLine,
            boldColor = gridLineBold,
        )
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
        val width = if (isBold) 2.5f else 0.8f

        drawLine(
            color = color,
            start = Offset(x = pos, y = 0f),
            end = Offset(x = pos, y = size.height),
            strokeWidth = width,
        )

        drawLine(
            color = color,
            start = Offset(x = 0f, y = pos),
            end = Offset(x = size.width, y = pos),
            strokeWidth = width,
        )
    }
}

private fun DrawScope.drawSplashNumbers(
    cellSize: Float,
    visibleCells: Set<GridPoint>,
    initialFilled: Set<GridPoint>,
    solvedGrid: Array<IntArray>,
    fixedColor: Color,
    fillingColor: Color,
    textMeasurer: TextMeasurer,
    density: Density,
) {
    val fontSizeSp = with(density) { (cellSize * 0.42f).toSp() }

    for (cell in visibleCells) {
        val num = solvedGrid[cell.row][cell.col]
        val isInitial = cell in initialFilled
        val color = if (isInitial) fixedColor else fillingColor

        val style = TextStyle(
            fontSize = fontSizeSp,
            fontWeight = if (isInitial) FontWeight.Medium else FontWeight.Normal,
            color = color,
        )

        val text = num.toString()
        val measured = textMeasurer.measure(text, style)
        val x = cell.col * cellSize + (cellSize - measured.size.width) / 2f
        val y = cell.row * cellSize + (cellSize - measured.size.height) / 2f
        drawText(measured, topLeft = Offset(x, y))
    }
}