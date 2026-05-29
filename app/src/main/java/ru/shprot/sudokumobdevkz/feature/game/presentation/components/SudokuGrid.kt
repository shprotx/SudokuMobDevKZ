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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import ru.shprot.sudokumobdevkz.R
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.game.domain.model.CellData

@Composable
fun SudokuGrid(
    modifier: Modifier,
    cells: List<List<CellData>>,
    selectedRow: Int,
    selectedCol: Int,
    isPaused: Boolean = false,
    highlightedNumber: Int = 0,
    onCellClick: (row: Int, col: Int) -> Unit,
    onCellLongClick: ((row: Int, col: Int) -> Unit)? = null,
) {
    val gridLine = AppTheme.colors.gridLine
    val gridLineBold = AppTheme.colors.gridLineBold
    val cellSelected = AppTheme.colors.cellSelected
    val cellHighlight = AppTheme.colors.cellHighlight
    val cellSameNumber = AppTheme.colors.cellSameNumber
    val cellErrorColor = AppTheme.colors.cellError
    val background = AppTheme.colors.backgroundCard
    val fixedColor = AppTheme.colors.cellFixed
    val editableColor = AppTheme.colors.cellEditable
    val errorColor = AppTheme.colors.error
    val draftColor = AppTheme.colors.draftText
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val hapticFeedback = LocalHapticFeedback.current

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(onCellLongClick) {
                detectTapGestures(
                    onTap = { offset ->
                        val cellSize = size.width / 9f
                        val col = (offset.x / cellSize).toInt().coerceIn(0, 8)
                        val row = (offset.y / cellSize).toInt().coerceIn(0, 8)
                        onCellClick(row, col)
                    },
                    onLongPress = { offset ->
                        if (onCellLongClick != null) {
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                            val cellSize = size.width / 9f
                            val col = (offset.x / cellSize).toInt().coerceIn(0, 8)
                            val row = (offset.y / cellSize).toInt().coerceIn(0, 8)
                            onCellLongClick(row, col)
                        }
                    },
                )
            },
    ) {
        val cellSize = size.width / 9f

        drawRect(color = background, size = size)

        if (!isPaused) {
            drawHighlights(cells, selectedRow, selectedCol, cellSize, cellSelected, cellHighlight, cellSameNumber, cellErrorColor, highlightedNumber)

            drawNumbers(cells, cellSize, fixedColor, editableColor, errorColor, draftColor, highlightedNumber, textMeasurer, density)
        }

        drawGridLines(cellSize, gridLine, gridLineBold)
    }
}

private fun DrawScope.drawHighlights(
    cells: List<List<CellData>>,
    selectedRow: Int,
    selectedCol: Int,
    cellSize: Float,
    cellSelected: Color,
    cellHighlight: Color,
    cellSameNumber: Color,
    cellErrorColor: Color,
    highlightedNumber: Int,
) {
    for (r in 0 until 9) {
        for (c in 0 until 9) {
            val cell = cells[r][c]

            if (cell.isError) {
                drawRect(
                    color = cellErrorColor,
                    topLeft = Offset(c * cellSize, r * cellSize),
                    size = Size(cellSize, cellSize),
                )
            }

            if (highlightedNumber > 0 && cell.value == highlightedNumber && !cell.isError) {
                drawRect(
                    color = cellSameNumber,
                    topLeft = Offset(c * cellSize, r * cellSize),
                    size = Size(cellSize, cellSize),
                )
            }
        }
    }

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

private fun DrawScope.drawNumbers(
    cells: List<List<CellData>>,
    cellSize: Float,
    fixedColor: Color,
    editableColor: Color,
    errorColor: Color,
    draftColor: Color,
    highlightedNumber: Int,
    textMeasurer: TextMeasurer,
    density: Density,
) {
    val gridFont = FontFamily(Font(R.font.ibm_plex_mono_bold))
    val fontSizeSp = with(density) { (cellSize * 0.45f).toSp() }
    val draftFontSizeSp = with(density) { (cellSize * 0.24f).toSp() }
    val draftHighlightFontSizeSp = with(density) { (cellSize * 0.27f).toSp() }

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
                    fontFamily = gridFont,
                    fontSize = fontSizeSp,
                    fontWeight = if (cell.isGiven) FontWeight.Bold else FontWeight.SemiBold,
                    color = color,
                )
                val text = cell.value.toString()
                val measured = textMeasurer.measure(text, style)
                val x = col * cellSize + (cellSize - measured.size.width) / 2f
                val y = row * cellSize + (cellSize - measured.size.height) / 2f
                drawText(measured, topLeft = Offset(x, y))
            } else if (cell.notes.isNotEmpty()) {
                val inset = cellSize * 0.06f
                val innerSize = cellSize - inset * 2
                val noteSize = innerSize / 3f
                for (note in cell.notes) {
                    val isHighlighted = note == highlightedNumber
                    val noteStyle = TextStyle(
                        fontSize = if (isHighlighted) draftHighlightFontSizeSp else draftFontSizeSp,
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Medium,
                        color = if (isHighlighted) fixedColor else draftColor,
                    )
                    val noteRow = (note - 1) / 3
                    val noteCol = (note - 1) % 3
                    val text = note.toString()
                    val measured = textMeasurer.measure(text, noteStyle)
                    val x = col * cellSize + inset + noteCol * noteSize + (noteSize - measured.size.width) / 2f
                    val y = row * cellSize + inset + noteRow * noteSize + (noteSize - measured.size.height) / 2f
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
        val width = if (isBold) 3f else 1f

        drawLine(color, Offset(pos, 0f), Offset(pos, size.height), width)

        drawLine(color, Offset(0f, pos), Offset(size.width, pos), width)
    }
}
