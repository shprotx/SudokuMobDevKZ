package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun DraftNotesPopup(
    row: Int,
    col: Int,
    notes: Set<Int>,
    gridBounds: Rect,
    onNumberClick: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val positionProvider = remember(row, col, gridBounds) {
        DraftPopupPositionProvider(row, col, gridBounds)
    }
    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true),
    ) {
        DraftPopupContent(notes = notes, onNumberClick = onNumberClick)
    }
}

@Composable
internal fun DraftPopupContent(
    notes: Set<Int>,
    onNumberClick: (Int) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium),
        color = AppTheme.colors.backgroundCard,
        shadowElevation = AppTheme.sizes.elevationMedium,
    ) {
        Column(
            modifier = Modifier.padding(AppTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.extraSmall),
        ) {
            for (rowIndex in 0 until 3) {
                Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.extraSmall)) {
                    for (colIndex in 0 until 3) {
                        val number = rowIndex * 3 + colIndex + 1
                        DraftNumberButton(
                            number = number,
                            isSelected = number in notes,
                            onClick = { onNumberClick(number) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun DraftNumberButton(
    number: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusSmall)
    val backgroundColor = if (isSelected) AppTheme.colors.primary else AppTheme.colors.surface
    val textColor = if (isSelected) AppTheme.colors.textOnPrimary else AppTheme.colors.text
    val description = stringResource(R.string.draft_notes_popup_number_description, number)

    Box(
        modifier = Modifier
            .size(AppTheme.sizes.numberPanelButton)
            .clip(shape)
            .background(backgroundColor, shape)
            .border(
                width = AppTheme.sizes.dividerThickness,
                color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.divider,
                shape = shape,
            )
            .semantics { contentDescription = description }
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = number.toString(),
            style = AppTheme.typography.body1,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
        )
    }
}

private class DraftPopupPositionProvider(
    private val row: Int,
    private val col: Int,
    private val gridBounds: Rect,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val cellSize = gridBounds.width / 9f
        val cellLeft = gridBounds.left + col * cellSize
        val cellTop = gridBounds.top + row * cellSize
        val cellBottom = cellTop + cellSize

        val margin = 8

        var x = (cellLeft + cellSize / 2f - popupContentSize.width / 2f).toInt()
        x = x.coerceIn(
            margin,
            (windowSize.width - popupContentSize.width - margin).coerceAtLeast(margin),
        )

        val yBelow = (cellBottom + margin).toInt()
        val yAbove = (cellTop - popupContentSize.height - margin).toInt()

        val y = when {
            yBelow + popupContentSize.height <= windowSize.height -> yBelow
            yAbove >= 0 -> yAbove
            else -> (windowSize.height - popupContentSize.height - margin).coerceAtLeast(margin)
        }

        return IntOffset(x, y)
    }
}