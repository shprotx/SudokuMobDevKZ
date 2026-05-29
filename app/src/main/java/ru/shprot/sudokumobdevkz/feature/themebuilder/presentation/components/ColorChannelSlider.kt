package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun ColorChannelSlider(
    label: String,
    value: Int,
    gradient: List<Color>,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
    ) {
        Text(
            modifier = Modifier.width(AppTheme.sizes.iconMedium),
            text = label,
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary,
            textAlign = TextAlign.Center,
        )

        ChannelTrack(
            modifier = Modifier.weight(1f),
            value = value,
            gradient = gradient,
            onValueChange = onValueChange,
        )

        Text(
            modifier = Modifier.width(AppTheme.sizes.iconLarge),
            text = value.toString(),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.text,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
private fun ChannelTrack(
    value: Int,
    gradient: List<Color>,
    onValueChange: (Int) -> Unit,
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
                        onValueChange(((offset.x / widthPx) * MAX_CHANNEL).toInt().coerceIn(0, MAX_CHANNEL))
                    }
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    if (widthPx > 0) {
                        val x = change.position.x.coerceIn(0f, widthPx.toFloat())
                        onValueChange(((x / widthPx) * MAX_CHANNEL).toInt().coerceIn(0, MAX_CHANNEL))
                    }
                }
            },
    ) {
        val knobX = (value.toFloat() / MAX_CHANNEL * size.width).coerceIn(0f, size.width)
        drawLine(
            color = knobColor,
            start = Offset(knobX, 0f),
            end = Offset(knobX, size.height),
            strokeWidth = knobWidth.toPx() * 2,
            cap = StrokeCap.Round,
        )
        drawCircle(
            color = knobColor,
            radius = size.height / 2.6f,
            center = Offset(knobX, size.height / 2f),
            style = Stroke(width = knobWidth.toPx() * 2, cap = StrokeCap.Round),
        )
    }
}

private const val MAX_CHANNEL = 255
