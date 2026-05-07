package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementIconKey
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementDecoration
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementVisual
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.util.AchievementVisuals
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun AchievementIcon(
    modifier: Modifier,
    iconKey: AchievementIconKey,
) {
    val visual = AchievementVisuals.resolve(iconKey)
    val emojiFontSize = AppTheme.sizes.iconXL.value.times(0.42f).sp

    Box(
        modifier = modifier.size(AppTheme.sizes.iconXL),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawAchievementBadge(visual)
        }

        Text(
            text = visual.emoji,
            style = TextStyle(fontSize = emojiFontSize),
        )
    }
}

private fun DrawScope.drawAchievementBadge(visual: AchievementVisual) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f

    drawDecoration(visual = visual, center = center, radius = radius)

    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(visual.gradientStart, visual.gradientEnd),
            start = Offset(0f, 0f),
            end = Offset(size.width, size.height),
        ),
        radius = radius * 0.78f,
        center = center,
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
            center = Offset(center.x - radius * 0.25f, center.y - radius * 0.3f),
            radius = radius * 0.55f,
        ),
        radius = radius * 0.78f,
        center = center,
    )

    drawCircle(
        color = visual.ringColor,
        radius = radius * 0.78f,
        center = center,
        style = Stroke(width = radius * 0.06f),
    )
}

private fun DrawScope.drawDecoration(
    visual: AchievementVisual,
    center: Offset,
    radius: Float,
) {
    when (visual.decoration) {
        AchievementDecoration.RAYS -> drawRays(center, radius, visual.accentColor)
        AchievementDecoration.SPARKLES -> drawSparkles(center, radius, visual.accentColor)
        AchievementDecoration.STARS -> drawStars(center, radius, visual.accentColor)
        AchievementDecoration.FLAMES -> drawFlames(center, radius, visual.accentColor, visual.gradientStart)
        AchievementDecoration.HALO -> drawHalo(center, radius, visual.accentColor)
        AchievementDecoration.BOLTS -> drawBolts(center, radius, visual.accentColor)
        AchievementDecoration.NONE -> Unit
    }
}

private fun DrawScope.drawRays(center: Offset, radius: Float, color: Color) {
    val rayCount = 12
    val rayLength = radius * 0.96f
    val innerRadius = radius * 0.82f
    repeat(rayCount) { i ->
        val angle = (i * (360f / rayCount)) * (Math.PI / 180f).toFloat()
        val start = Offset(
            x = center.x + innerRadius * cos(angle),
            y = center.y + innerRadius * sin(angle),
        )
        val end = Offset(
            x = center.x + rayLength * cos(angle),
            y = center.y + rayLength * sin(angle),
        )
        drawLine(
            color = color,
            start = start,
            end = end,
            strokeWidth = radius * 0.07f,
            cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawSparkles(center: Offset, radius: Float, color: Color) {
    val sparklePositions = listOf(
        -0.85f to -0.55f,
        0.85f to -0.45f,
        -0.7f to 0.7f,
        0.75f to 0.7f,
        0f to -0.95f,
    )
    sparklePositions.forEachIndexed { index, (dx, dy) ->
        val pos = Offset(center.x + dx * radius, center.y + dy * radius)
        val sparkleSize = radius * (if (index % 2 == 0) 0.18f else 0.12f)
        drawSparkle(pos, sparkleSize, color)
    }
}

private fun DrawScope.drawSparkle(center: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - size)
        lineTo(center.x + size * 0.25f, center.y - size * 0.25f)
        lineTo(center.x + size, center.y)
        lineTo(center.x + size * 0.25f, center.y + size * 0.25f)
        lineTo(center.x, center.y + size)
        lineTo(center.x - size * 0.25f, center.y + size * 0.25f)
        lineTo(center.x - size, center.y)
        lineTo(center.x - size * 0.25f, center.y - size * 0.25f)
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawStars(center: Offset, radius: Float, color: Color) {
    val starPositions = listOf(
        -0.85f to -0.6f,
        0.8f to -0.7f,
        -0.75f to 0.65f,
        0.85f to 0.55f,
        -0.95f to 0.05f,
        0.95f to -0.05f,
    )
    starPositions.forEachIndexed { index, (dx, dy) ->
        val pos = Offset(center.x + dx * radius, center.y + dy * radius)
        val starSize = radius * (if (index % 2 == 0) 0.13f else 0.09f)
        drawCircle(color = color, radius = starSize, center = pos)
    }
}

private fun DrawScope.drawFlames(
    center: Offset,
    radius: Float,
    accent: Color,
    glow: Color,
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(glow.copy(alpha = 0.5f), Color.Transparent),
            center = center,
            radius = radius,
        ),
        radius = radius,
        center = center,
    )
    val flamePositions = listOf(
        Triple(-0.8f, -0.3f, 0.18f),
        Triple(0.8f, -0.3f, 0.18f),
        Triple(-0.6f, -0.75f, 0.13f),
        Triple(0.6f, -0.75f, 0.13f),
        Triple(0f, -0.95f, 0.16f),
    )
    flamePositions.forEach { (dx, dy, sizeFactor) ->
        drawTeardrop(
            tip = Offset(center.x + dx * radius, center.y + dy * radius),
            size = radius * sizeFactor,
            color = accent,
        )
    }
}

private fun DrawScope.drawTeardrop(tip: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(tip.x, tip.y - size)
        cubicTo(
            tip.x + size, tip.y - size * 0.3f,
            tip.x + size * 0.6f, tip.y + size * 0.7f,
            tip.x, tip.y + size,
        )
        cubicTo(
            tip.x - size * 0.6f, tip.y + size * 0.7f,
            tip.x - size, tip.y - size * 0.3f,
            tip.x, tip.y - size,
        )
        close()
    }
    drawPath(path = path, color = color)
}

private fun DrawScope.drawHalo(center: Offset, radius: Float, color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.55f),
        radius = radius * 0.95f,
        center = center,
        style = Stroke(width = radius * 0.05f),
    )
    drawCircle(
        color = color.copy(alpha = 0.3f),
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.05f),
    )
    drawSparkles(center, radius, color)
}

private fun DrawScope.drawBolts(center: Offset, radius: Float, color: Color) {
    val angles = listOf(35f, 145f, 255f)
    angles.forEach { angle ->
        rotate(degrees = angle, pivot = center) {
            val pos = Offset(center.x, center.y - radius * 0.85f)
            drawBolt(pos, radius * 0.16f, color)
        }
    }
}

private fun DrawScope.drawBolt(top: Offset, size: Float, color: Color) {
    val path = Path().apply {
        moveTo(top.x + size * 0.2f, top.y)
        lineTo(top.x - size * 0.5f, top.y + size * 0.9f)
        lineTo(top.x - size * 0.05f, top.y + size * 0.9f)
        lineTo(top.x - size * 0.3f, top.y + size * 1.7f)
        lineTo(top.x + size * 0.6f, top.y + size * 0.7f)
        lineTo(top.x + size * 0.15f, top.y + size * 0.7f)
        lineTo(top.x + size * 0.45f, top.y)
        close()
    }
    drawPath(path = path, color = color)
}
