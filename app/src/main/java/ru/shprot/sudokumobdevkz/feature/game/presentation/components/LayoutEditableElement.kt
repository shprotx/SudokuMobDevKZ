package ru.shprot.sudokumobdevkz.feature.game.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

private const val WIGGLE_DEGREES = 0.4f
private const val WIGGLE_DURATION_MS = 190
private const val WIGGLE_PHASE_STEP_MS = 60

@Composable
internal fun LayoutEditableElement(
    modifier: Modifier,
    overlayModifier: Modifier,
    wiggleIndex: Int,
    isDragging: Boolean,
    content: @Composable () -> Unit,
) {
    val transition = rememberInfiniteTransition(label = "layoutWiggle")
    val angle by transition.animateFloat(
        initialValue = -WIGGLE_DEGREES,
        targetValue = WIGGLE_DEGREES,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = WIGGLE_DURATION_MS, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(wiggleIndex * WIGGLE_PHASE_STEP_MS),
        ),
        label = "layoutWiggleAngle",
    )
    val borderColor = if (isDragging) AppTheme.colors.primary else AppTheme.colors.textSecondary.copy(alpha = 0.4f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer { rotationZ = if (isDragging) 0f else angle }
            .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
            .border(
                width = AppTheme.sizes.dividerThickness,
                color = borderColor,
                shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium),
            )
            .padding(vertical = AppTheme.paddings.small),
    ) {
        content()

        Box(
            modifier = Modifier
                .matchParentSize()
                .then(overlayModifier)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {},
        )
    }
}
