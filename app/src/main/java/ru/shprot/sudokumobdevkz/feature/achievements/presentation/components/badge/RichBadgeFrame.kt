package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementVisual

internal object RichBadgePalette {
    val ringOuterEdge = Color(0xFF9C6A00)
    val ringTop = Color(0xFFFFE08A)
    val ringBottom = Color(0xFFE39812)
    val ringInnerEdge = Color(0xFFFFF3C4)
    val ringShadow = Color(0x33000000)
    val sparkleWhite = Color(0xFFFFFFFF)
}

internal fun DrawScope.drawRichBadgeScene(visual: AchievementVisual) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f
    val sceneRadius = radius * 0.80f

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                visual.gradientStart,
                visual.gradientEnd,
                darken(visual.gradientEnd, 0.55f),
            ),
            center = Offset(center.x, center.y - sceneRadius * 0.25f),
            radius = sceneRadius * 1.35f,
        ),
        radius = sceneRadius,
        center = center,
    )

    drawStarDust(center, sceneRadius, visual.accentColor)
}

internal fun DrawScope.drawRichBadgeRing() {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f
    val ringWidth = radius * 0.17f
    val ringRadius = radius * 0.885f

    drawCircle(
        color = RichBadgePalette.ringShadow,
        radius = ringRadius + ringWidth * 0.62f,
        center = Offset(center.x, center.y + radius * 0.03f),
    )

    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(RichBadgePalette.ringTop, RichBadgePalette.ringBottom),
            start = Offset(center.x, center.y - radius),
            end = Offset(center.x, center.y + radius),
        ),
        radius = ringRadius,
        center = center,
        style = Stroke(width = ringWidth),
    )

    drawCircle(
        color = RichBadgePalette.ringOuterEdge,
        radius = ringRadius + ringWidth / 2f,
        center = center,
        style = Stroke(width = radius * 0.03f),
    )

    drawCircle(
        color = RichBadgePalette.ringInnerEdge,
        radius = ringRadius - ringWidth / 2f,
        center = center,
        style = Stroke(width = radius * 0.025f),
    )

    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent),
            start = Offset(center.x - radius * 0.5f, center.y - radius),
            end = Offset(center.x, center.y - radius * 0.2f),
        ),
        radius = ringRadius,
        center = center,
        style = Stroke(width = ringWidth * 0.55f),
    )
}

internal fun DrawScope.drawRichBadgeGloss() {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.28f), Color.Transparent),
            center = Offset(center.x - radius * 0.30f, center.y - radius * 0.38f),
            radius = radius * 0.62f,
        ),
        radius = radius * 0.80f,
        center = center,
    )
}

private fun DrawScope.drawStarDust(center: Offset, sceneRadius: Float, accent: Color) {
    val dots = listOf(
        Offset(-0.55f, -0.42f) to 0.030f,
        Offset(0.48f, -0.55f) to 0.022f,
        Offset(-0.30f, 0.58f) to 0.026f,
        Offset(0.60f, 0.35f) to 0.020f,
        Offset(0.12f, -0.68f) to 0.018f,
        Offset(-0.66f, 0.15f) to 0.020f,
        Offset(0.35f, 0.66f) to 0.016f,
    )
    dots.forEachIndexed { index, (rel, sizeFactor) ->
        drawCircle(
            color = RichBadgePalette.sparkleWhite.copy(alpha = if (index % 2 == 0) 0.75f else 0.45f),
            radius = sceneRadius * sizeFactor,
            center = Offset(center.x + rel.x * sceneRadius, center.y + rel.y * sceneRadius),
        )
    }

    drawFourPointSparkle(
        center = Offset(center.x - sceneRadius * 0.52f, center.y - sceneRadius * 0.58f),
        size = sceneRadius * 0.14f,
        color = accent,
    )
    drawFourPointSparkle(
        center = Offset(center.x + sceneRadius * 0.58f, center.y + sceneRadius * 0.52f),
        size = sceneRadius * 0.10f,
        color = accent.copy(alpha = 0.8f),
    )
}

internal fun DrawScope.drawFourPointSparkle(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - size)
        quadraticTo(center.x + size * 0.12f, center.y - size * 0.12f, center.x + size, center.y)
        quadraticTo(center.x + size * 0.12f, center.y + size * 0.12f, center.x, center.y + size)
        quadraticTo(center.x - size * 0.12f, center.y + size * 0.12f, center.x - size, center.y)
        quadraticTo(center.x - size * 0.12f, center.y - size * 0.12f, center.x, center.y - size)
        close()
    }
    drawPath(path = path, color = color)
}

internal fun darken(color: Color, factor: Float): Color = Color(
    red = color.red * factor,
    green = color.green * factor,
    blue = color.blue * factor,
    alpha = color.alpha,
)

internal fun lighten(color: Color, factor: Float): Color = Color(
    red = color.red + (1f - color.red) * factor,
    green = color.green + (1f - color.green) * factor,
    blue = color.blue + (1f - color.blue) * factor,
    alpha = color.alpha,
)