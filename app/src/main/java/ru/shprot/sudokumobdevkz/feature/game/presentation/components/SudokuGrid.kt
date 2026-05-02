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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.CellData

@Composable
fun SudokuGrid(
    modifier: Modifier = Modifier,
    cells: Array<Array<CellData>>,
    selectedRow: Int,
    selectedCol: Int,
    onCellClick: (row: Int, col: Int) -> Unit,
) {
    val gridLine = AppTheme.colors.gridLine
    val gridLineBold = AppTheme.colors.gridLineBold
    val cellSelected = AppTheme.colors.cellSelected
    val cellHighlight = AppTheme.colors.cellHighlight
    val cellErrorColor = AppTheme.colors.cellError
    val background = AppTheme.colors.backgroundCard
    val fixedColor = AppTheme.colors.cellFixed
    val editableColor = AppTheme.colors.cellEditable
    val errorColor = AppTheme.colors.error
    val draftColor = AppTheme.colors.draftText
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

        drawHighlights(cells, selectedRow, selectedCol, cellSize, cellSelected, cellHighlight, cellErrorColor)

        drawNumbers(cells, cellSize, fixedColor, editableColor, errorColor, draftColor, textMeasurer, density)

        drawGridLines(cellSize, gridLine, gridLineBold)
    }
}

private fun DrawScope.drawHighlights(
    cells: Array<Array<CellData>>,
    selectedRow: Int,
    selectedCol: Int,
    cellSize: Float,
    cellSelected: Color,
    cellHighlight: Color,
    cellErrorColor: Color,
) {
    // Error cells
    for (r in 0 until 9) {
        for (c in 0 until 9) {
            if (cells[r][c].isError) {
                drawRect(
                    color = cellErrorColor,
                    topLeft = Offset(c * cellSize, r * cellSize),
                    size = Size(cellSize, cellSize),
                )
            }
        }
    }

    if (selectedRow < 0 || selectedCol < 0) return

    // Row + column
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

    // Region
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

    // Selected cell
    drawRect(
        color = cellSelected,
        topLeft = Offset(selectedCol * cellSize, selectedRow * cellSize),
        size = Size(cellSize, cellSize),
    )
}

private fun DrawScope.drawNumbers(
    cells: Array<Array<CellData>>,
    cellSize: Float,
    fixedColor: Color,
    editableColor: Color,
    errorColor: Color,
    draftColor: Color,
    textMeasurer: TextMeasurer,
    density: Density,
) {
    val fontSizeSp = with(density) { (cellSize * 0.45f).toSp() }
    val draftFontSizeSp = with(density) { (cellSize * 0.18f).toSp() }

    for (row in 0 until 9) {
        for (col in 0 until 9) {
            val cell = cells[row][col]

            if (cell.value != 0) {
                val color = when {
                    cell.isError -> errorColor
                    cell.isGiven -> fixedColor
                    else -> editableColor
                }
                val style = TextStyle(
                    fontSize = fontSizeSp,
                    fontWeight = if (cell.isGiven) FontWeight.SemiBold else FontWeight.Normal,
                    color = color,
                )
                val text = cell.value.toString()
                val measured = textMeasurer.measure(text, style)
                val x = col * cellSize + (cellSize - measured.size.width) / 2f
                val y = row * cellSize + (cellSize - measured.size.height) / 2f
                drawText(measured, topLeft = Offset(x, y))
            } else if (cell.notes.isNotEmpty()) {
                val noteStyle = TextStyle(
                    fontSize = draftFontSizeSp,
                    fontWeight = FontWeight.Normal,
                    color = draftColor,
                )
                val noteSize = cellSize / 3f
                for (note in cell.notes) {
                    val noteRow = (note - 1) / 3
                    val noteCol = (note - 1) % 3
                    val text = note.toString()
                    val measured = textMeasurer.measure(text, noteStyle)
                    val x = col * cellSize + noteCol * noteSize + (noteSize - measured.size.width) / 2f
                    val y = row * cellSize + noteRow * noteSize + (noteSize - measured.size.height) / 2f
                    drawText(measured, topLeft = Offset(x, y))
                }
            }
        }
    }
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
