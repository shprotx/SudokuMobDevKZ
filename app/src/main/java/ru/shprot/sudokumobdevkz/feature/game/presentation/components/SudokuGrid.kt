package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun SudokuGrid(
    modifier: Modifier = Modifier,
    selectedRow: Int,
    selectedCol: Int,
    onCellClick: (row: Int, col: Int) -> Unit,
) {
    val gridLine = AppTheme.colors.gridLine
    val gridLineBold = AppTheme.colors.gridLineBold
    val cellSelected = AppTheme.colors.cellSelected
    val cellHighlight = AppTheme.colors.cellHighlight
    val background = AppTheme.colors.backgroundCard
    val textColor = AppTheme.colors.cellFixed
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellSize = size.width / 9f
                    val col = (offset.x / cellSize).toInt().coerceIn(0, 8)
                    val row = (offset.y / cellSize).toInt().coerceIn(0, 8)
                    onCellClick(row, col)
                }
            },
    ) {
        val cellSize = size.width / 9f

        drawRect(color = background, size = size)

        drawHighlights(selectedRow, selectedCol, cellSize, cellSelected, cellHighlight)

        drawGridLines(cellSize, gridLine, gridLineBold)

        drawPlaceholderNumbers(cellSize, textColor, textMeasurer, density)
    }
}

private fun DrawScope.drawHighlights(
    selectedRow: Int,
    selectedCol: Int,
    cellSize: Float,
    cellSelected: Color,
    cellHighlight: Color,
) {
    if (selectedRow < 0 || selectedCol < 0) return

    for (i in 0 until 9) {
        drawRect(
            color = cellHighlight,
            topLeft = Offset(i * cellSize, selectedRow * cellSize),
            size = Size(cellSize, cellSize),
        )

        drawRect(
            color = cellHighlight,
            topLeft = Offset(selectedCol * cellSize, i * cellSize),
            size = Size(cellSize, cellSize),
        )
    }

    val regionStartRow = (selectedRow / 3) * 3
    val regionStartCol = (selectedCol / 3) * 3
    for (r in regionStartRow until regionStartRow + 3) {
        for (c in regionStartCol until regionStartCol + 3) {
            drawRect(
                color = cellHighlight,
                topLeft = Offset(c * cellSize, r * cellSize),
                size = Size(cellSize, cellSize),
            )
        }
    }

    drawRect(
        color = cellSelected,
        topLeft = Offset(selectedCol * cellSize, selectedRow * cellSize),
        size = Size(cellSize, cellSize),
    )
}

private fun DrawScope.drawGridLines(
    cellSize: Float,
    thinColor: Color,
    boldColor: Color,
) {
    for (i in 0..9) {
        val pos = i * cellSize
        val isBold = i % 3 == 0
        val color = if (isBold) boldColor else thinColor
        val width = if (isBold) 2.5f else 0.5f

        drawLine(color, Offset(pos, 0f), Offset(pos, size.height), width)

        drawLine(color, Offset(0f, pos), Offset(size.width, pos), width)
    }
}

private fun DrawScope.drawPlaceholderNumbers(
    cellSize: Float,
    textColor: Color,
    textMeasurer: TextMeasurer,
    density: androidx.compose.ui.unit.Density,
) {
    val demoNumbers = listOf(
        Triple(0, 1, 9), Triple(0, 5, 3),
        Triple(1, 2, 1), Triple(1, 6, 8), Triple(1, 7, 7),
        Triple(3, 0, 7), Triple(3, 5, 2),
        Triple(5, 0, 5), Triple(5, 1, 3), Triple(5, 2, 2), Triple(5, 6, 4), Triple(5, 8, 9),
        Triple(6, 1, 4), Triple(6, 4, 5), Triple(6, 6, 1),
        Triple(7, 0, 9),
        Triple(8, 1, 5), Triple(8, 2, 4), Triple(8, 3, 2), Triple(8, 4, 8),
    )

    val fontSizeSp = with(density) { (cellSize * 0.45f).toSp() }

    val style = TextStyle(
        fontSize = fontSizeSp,
        fontWeight = FontWeight.Medium,
        color = textColor,
    )

    for ((row, col, num) in demoNumbers) {
        val text = num.toString()
        val measured = textMeasurer.measure(text, style)
        val x = col * cellSize + (cellSize - measured.size.width) / 2f
        val y = row * cellSize + (cellSize - measured.size.height) / 2f
        drawText(measured, topLeft = Offset(x, y))
    }
}
