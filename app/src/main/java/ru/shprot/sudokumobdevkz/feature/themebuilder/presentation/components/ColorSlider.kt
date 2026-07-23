package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ColorSlider(
    value: Float,
    gradient: List<Color>,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val knobWidth = AppTheme.sizes.colorSliderKnob
    val sliderHeight = AppTheme.sizes.colorSliderHeight
    val cornerRadius = AppTheme.sizes.cornerRadiusSmall
    val knobColor = AppTheme.colors.backgroundCard
    val knobBorder = AppTheme.colors.text
    var widthPx by remember { mutableIntStateOf(0) }
    val brush = remember(gradient) { Brush.horizontalGradient(gradient) }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(sliderHeight)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
            .border(AppTheme.sizes.dividerThickness, knobBorder.copy(alpha = 0.2f), RoundedCornerShape(cornerRadius))
            .onSizeChanged { widthPx = it.width }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (widthPx > 0) {
                        onValueChange((offset.x / widthPx).coerceIn(0f, 1f))
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    if (widthPx > 0) {
                        onValueChange((change.position.x / widthPx).coerceIn(0f, 1f))
                    }
                }
            },
    ) {
        val knobX = (value * size.width).coerceIn(0f, size.width)
        drawLine(
            color = knobColor,
            start = Offset(knobX, 0f),
            end = Offset(knobX, size.height / 4f),
            strokeWidth = knobWidth.toPx(),
            cap = StrokeCap.Round,
        )
        drawCircle(
            color = knobColor,
            radius = size.height / 4f,
            center = Offset(knobX, size.height / 2f),
            style = Stroke(width = knobWidth.toPx(), cap = StrokeCap.Round),
        )
        drawLine(
            color = knobColor,
            start = Offset(knobX, size.height * 3f / 4f),
            end = Offset(knobX, size.height),
            strokeWidth = knobWidth.toPx(),
            cap = StrokeCap.Round,
        )
    }
}