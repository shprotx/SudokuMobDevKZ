package ru.shprot.sudokumobdevkz.core.uicommon.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
internal fun FitHeightBox(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {

    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val measurable = measurables.firstOrNull()
            ?: return@Layout layout(constraints.minWidth, constraints.minHeight) {}

        val placeable = measurable.measure(
            constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity),
        )
        val availableHeight = constraints.maxHeight
        val isBounded = availableHeight != Constraints.Infinity
        val scale = when {
            !isBounded || placeable.height <= availableHeight || placeable.height <= 0 -> 1f
            else -> availableHeight.toFloat() / placeable.height
        }
        val height = when {
            isBounded -> minOf(placeable.height, availableHeight)
            else -> placeable.height
        }

        layout(placeable.width, height) {
            placeable.placeWithLayer(x = 0, y = 0) {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0f)
            }
        }
    }
}