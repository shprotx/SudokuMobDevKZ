package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate

internal data class MetalPalette(
    val light: Color,
    val mid: Color,
    val dark: Color,
    val outline: Color,
)

internal val bronzeMetal = MetalPalette(
    light = Color(0xFFF2C9A0),
    mid = Color(0xFFCD7F32),
    dark = Color(0xFF8C4A16),
    outline = Color(0xFF5E2F0A),
)

internal val silverMetal = MetalPalette(
    light = Color(0xFFF4F7FB),
    mid = Color(0xFFB8C4D0),
    dark = Color(0xFF7A8794),
    outline = Color(0xFF4C5560),
)

internal val goldMetal = MetalPalette(
    light = Color(0xFFFFF2B0),
    mid = Color(0xFFFFC93C),
    dark = Color(0xFFC77800),
    outline = Color(0xFF8A5200),
)

internal fun DrawScope.drawTrophy(center: Offset, r: Float, metal: MetalPalette) {
    val cupTop = center.y - r * 0.95f
    val cupBottom = center.y + r * 0.18f
    val cupHalfWidth = r * 0.62f

    translate(left = r * 0.06f, top = r * 0.10f) {
        drawTrophySilhouette(center, r, Color(0x40000000))
    }

    drawTrophyHandle(center, r, metal, isLeft = true)
    drawTrophyHandle(center, r, metal, isLeft = false)

    val cup = Path().apply {
        moveTo(center.x - cupHalfWidth, cupTop)
        lineTo(center.x + cupHalfWidth, cupTop)
        cubicTo(
            center.x + cupHalfWidth, cupBottom - r * 0.10f,
            center.x + r * 0.30f, cupBottom,
            center.x, cupBottom,
        )
        cubicTo(
            center.x - r * 0.30f, cupBottom,
            center.x - cupHalfWidth, cupBottom - r * 0.10f,
            center.x - cupHalfWidth, cupTop,
        )
        close()
    }
    drawPath(
        path = cup,
        brush = Brush.linearGradient(
            colors = listOf(metal.light, metal.mid, metal.dark),
            start = Offset(center.x - cupHalfWidth, cupTop),
            end = Offset(center.x + cupHalfWidth, cupBottom),
        ),
    )
    drawPath(path = cup, color = metal.outline, style = Stroke(width = r * 0.055f))

    drawOval(
        brush = Brush.linearGradient(
            colors = listOf(metal.light, metal.dark),
            start = Offset(center.x, cupTop - r * 0.09f),
            end = Offset(center.x, cupTop + r * 0.09f),
        ),
        topLeft = Offset(center.x - cupHalfWidth, cupTop - r * 0.09f),
        size = Size(cupHalfWidth * 2f, r * 0.18f),
    )

    val stemTop = cupBottom
    val stemBottom = center.y + r * 0.55f
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.10f, stemTop)
            lineTo(center.x + r * 0.10f, stemTop)
            lineTo(center.x + r * 0.16f, stemBottom)
            lineTo(center.x - r * 0.16f, stemBottom)
            close()
        },
        brush = Brush.linearGradient(
            colors = listOf(metal.mid, metal.dark),
            start = Offset(center.x, stemTop),
            end = Offset(center.x, stemBottom),
        ),
    )

    val baseTop = stemBottom
    val baseHeight = r * 0.24f
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.44f, baseTop + baseHeight)
            lineTo(center.x + r * 0.44f, baseTop + baseHeight)
            lineTo(center.x + r * 0.34f, baseTop)
            lineTo(center.x - r * 0.34f, baseTop)
            close()
        },
        brush = Brush.linearGradient(
            colors = listOf(metal.mid, metal.dark),
            start = Offset(center.x, baseTop),
            end = Offset(center.x, baseTop + baseHeight),
        ),
    )

    drawPath(
        path = Path().apply {
            moveTo(center.x - cupHalfWidth * 0.55f, cupTop + r * 0.14f)
            cubicTo(
                center.x - cupHalfWidth * 0.68f, center.y - r * 0.30f,
                center.x - cupHalfWidth * 0.52f, center.y - r * 0.05f,
                center.x - cupHalfWidth * 0.34f, center.y + r * 0.04f,
            )
        },
        color = Color.White.copy(alpha = 0.65f),
        style = Stroke(width = r * 0.09f, cap = StrokeCap.Round),
    )
}

private fun DrawScope.drawTrophySilhouette(center: Offset, r: Float, color: Color) {
    drawOval(
        color = color,
        topLeft = Offset(center.x - r * 0.62f, center.y - r * 0.95f),
        size = Size(r * 1.24f, r * 1.1f),
    )
    drawRect(
        color = color,
        topLeft = Offset(center.x - r * 0.44f, center.y + r * 0.5f),
        size = Size(r * 0.88f, r * 0.28f),
    )
}

private fun DrawScope.drawTrophyHandle(center: Offset, r: Float, metal: MetalPalette, isLeft: Boolean) {
    val sign = if (isLeft) -1f else 1f
    val startX = center.x + sign * r * 0.60f
    drawPath(
        path = Path().apply {
            moveTo(startX, center.y - r * 0.80f)
            cubicTo(
                startX + sign * r * 0.55f, center.y - r * 0.85f,
                startX + sign * r * 0.55f, center.y - r * 0.15f,
                startX - sign * r * 0.02f, center.y - r * 0.12f,
            )
        },
        color = metal.dark,
        style = Stroke(width = r * 0.14f, cap = StrokeCap.Round),
    )
}

internal fun DrawScope.drawRibbonMedal(
    center: Offset,
    r: Float,
    metal: MetalPalette,
    ribbonLight: Color,
    ribbonDark: Color,
    numeral: Int?,
    withLaurels: Boolean,
    accent: Color,
) {
    listOf(-1f, 1f).forEach { sign ->
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * r * 0.08f, center.y - r * 1.05f)
                lineTo(center.x + sign * r * 0.62f, center.y - r * 1.05f)
                lineTo(center.x + sign * r * 0.34f, center.y - r * 0.05f)
                lineTo(center.x - sign * r * 0.12f, center.y - r * 0.28f)
                close()
            },
            brush = Brush.linearGradient(
                colors = listOf(ribbonLight, ribbonDark),
                start = Offset(center.x, center.y - r * 1.05f),
                end = Offset(center.x, center.y),
            ),
        )
    }

    val discCenter = Offset(center.x, center.y + r * 0.28f)
    val discRadius = r * 0.72f

    drawCircle(
        color = Color(0x40000000),
        radius = discRadius,
        center = Offset(discCenter.x + r * 0.05f, discCenter.y + r * 0.09f),
    )
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(metal.light, metal.mid, metal.dark),
            start = Offset(discCenter.x - discRadius, discCenter.y - discRadius),
            end = Offset(discCenter.x + discRadius, discCenter.y + discRadius),
        ),
        radius = discRadius,
        center = discCenter,
    )
    drawCircle(
        color = metal.outline,
        radius = discRadius,
        center = discCenter,
        style = Stroke(width = r * 0.055f),
    )
    drawCircle(
        color = metal.dark.copy(alpha = 0.55f),
        radius = discRadius * 0.74f,
        center = discCenter,
        style = Stroke(width = r * 0.04f),
    )

    if (withLaurels) {
        drawLaurelArc(discCenter, discRadius * 0.9f, metal.dark, isLeft = true)
        drawLaurelArc(discCenter, discRadius * 0.9f, metal.dark, isLeft = false)
    }

    if (numeral != null) {
        drawMedalNumeral(discCenter, discRadius, numeral, metal.outline, metal.light)
    } else {
        drawStar(discCenter, discRadius * 0.52f, metal.outline)
        drawStar(
            center = Offset(discCenter.x - r * 0.02f, discCenter.y - r * 0.03f),
            outerRadius = discRadius * 0.48f,
            color = accent,
        )
    }

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.5f), Color.Transparent),
            center = Offset(discCenter.x - discRadius * 0.35f, discCenter.y - discRadius * 0.4f),
            radius = discRadius * 0.6f,
        ),
        radius = discRadius,
        center = discCenter,
    )
}

private fun DrawScope.drawMedalNumeral(
    discCenter: Offset,
    discRadius: Float,
    numeral: Int,
    strokeColor: Color,
    fillColor: Color,
) {
    val h = discRadius * 0.62f
    val w = discRadius * 0.34f
    when (numeral) {
        1 -> {
            drawLine(
                color = strokeColor,
                start = Offset(discCenter.x, discCenter.y - h / 2f),
                end = Offset(discCenter.x, discCenter.y + h / 2f),
                strokeWidth = discRadius * 0.2f,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = strokeColor,
                start = Offset(discCenter.x, discCenter.y - h / 2f),
                end = Offset(discCenter.x - w * 0.6f, discCenter.y - h * 0.22f),
                strokeWidth = discRadius * 0.18f,
                cap = StrokeCap.Round,
            )
        }

        2 -> drawPath(
            path = Path().apply {
                moveTo(discCenter.x - w, discCenter.y - h * 0.28f)
                cubicTo(
                    discCenter.x - w, discCenter.y - h * 0.75f,
                    discCenter.x + w, discCenter.y - h * 0.75f,
                    discCenter.x + w * 0.9f, discCenter.y - h * 0.18f,
                )
                lineTo(discCenter.x - w * 0.85f, discCenter.y + h * 0.5f)
                lineTo(discCenter.x + w, discCenter.y + h * 0.5f)
            },
            color = strokeColor,
            style = Stroke(width = discRadius * 0.18f, cap = StrokeCap.Round),
        )

        else -> drawPath(
            path = Path().apply {
                moveTo(discCenter.x - w * 0.8f, discCenter.y - h * 0.42f)
                cubicTo(
                    discCenter.x + w * 1.1f, discCenter.y - h * 0.75f,
                    discCenter.x + w * 1.1f, discCenter.y - h * 0.05f,
                    discCenter.x - w * 0.15f, discCenter.y - h * 0.02f,
                )
                cubicTo(
                    discCenter.x + w * 1.2f, discCenter.y - h * 0.02f,
                    discCenter.x + w * 1.1f, discCenter.y + h * 0.72f,
                    discCenter.x - w * 0.85f, discCenter.y + h * 0.42f,
                )
            },
            color = strokeColor,
            style = Stroke(width = discRadius * 0.18f, cap = StrokeCap.Round),
        )
    }
}

private fun DrawScope.drawLaurelArc(center: Offset, radius: Float, color: Color, isLeft: Boolean) {
    val sign = if (isLeft) -1f else 1f
    repeat(4) { i ->
        val angle = Math.toRadians((110 + i * 22).toDouble() * sign.toDouble() + if (isLeft) 0.0 else 360.0)
        val base = Offset(
            center.x + (radius * Math.cos(angle)).toFloat(),
            center.y - (radius * Math.sin(angle)).toFloat(),
        )
        drawOval(
            color = color.copy(alpha = 0.85f),
            topLeft = Offset(base.x - radius * 0.10f, base.y - radius * 0.05f),
            size = Size(radius * 0.2f, radius * 0.1f),
        )
    }
}

internal fun DrawScope.drawCrown(center: Offset, r: Float, accent: Color) {
    val baseY = center.y + r * 0.55f
    val topY = center.y - r * 0.65f
    val halfW = r * 0.85f

    translate(left = r * 0.05f, top = r * 0.09f) {
        drawPath(path = crownPath(center, baseY, topY, halfW), color = Color(0x40000000))
    }

    drawPath(
        path = crownPath(center, baseY, topY, halfW),
        brush = Brush.linearGradient(
            colors = listOf(goldMetal.light, goldMetal.mid, goldMetal.dark),
            start = Offset(center.x, topY),
            end = Offset(center.x, baseY),
        ),
    )
    drawPath(
        path = crownPath(center, baseY, topY, halfW),
        color = goldMetal.outline,
        style = Stroke(width = r * 0.05f),
    )

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(goldMetal.mid, goldMetal.dark),
            start = Offset(center.x, baseY),
            end = Offset(center.x, baseY + r * 0.22f),
        ),
        topLeft = Offset(center.x - halfW, baseY),
        size = Size(halfW * 2f, r * 0.22f),
    )
    drawRect(
        color = goldMetal.outline,
        topLeft = Offset(center.x - halfW, baseY),
        size = Size(halfW * 2f, r * 0.22f),
        style = Stroke(width = r * 0.045f),
    )

    val gemPositions = listOf(-0.62f, 0f, 0.62f)
    val gemColors = listOf(Color(0xFFE0356B), Color(0xFF2E9BFF), Color(0xFF37C978))
    gemPositions.forEachIndexed { index, dx ->
        val gemCenter = Offset(center.x + dx * halfW, baseY + r * 0.11f)
        drawCircle(color = goldMetal.outline, radius = r * 0.085f, center = gemCenter)
        drawCircle(color = gemColors[index], radius = r * 0.065f, center = gemCenter)
        drawCircle(
            color = Color.White.copy(alpha = 0.8f),
            radius = r * 0.022f,
            center = Offset(gemCenter.x - r * 0.02f, gemCenter.y - r * 0.02f),
        )
    }

    val peakXs = listOf(-0.66f, 0f, 0.66f)
    peakXs.forEach { dx ->
        drawCircle(
            color = accent,
            radius = r * 0.09f,
            center = Offset(center.x + dx * halfW, topY + if (dx == 0f) -r * 0.12f else 0f),
        )
    }

    drawPath(
        path = Path().apply {
            moveTo(center.x - halfW * 0.55f, center.y - r * 0.05f)
            cubicTo(
                center.x - halfW * 0.45f, center.y + r * 0.2f,
                center.x - halfW * 0.2f, center.y + r * 0.32f,
                center.x - halfW * 0.05f, center.y + r * 0.34f,
            )
        },
        color = Color.White.copy(alpha = 0.5f),
        style = Stroke(width = r * 0.08f, cap = StrokeCap.Round),
    )
}

private fun crownPath(center: Offset, baseY: Float, topY: Float, halfW: Float): Path =
    Path().apply {
        moveTo(center.x - halfW, baseY)
        lineTo(center.x - halfW, topY + (baseY - topY) * 0.35f)
        lineTo(center.x - halfW * 0.38f, baseY - (baseY - topY) * 0.52f)
        lineTo(center.x, topY - (baseY - topY) * 0.1f)
        lineTo(center.x + halfW * 0.38f, baseY - (baseY - topY) * 0.52f)
        lineTo(center.x + halfW, topY + (baseY - topY) * 0.35f)
        lineTo(center.x + halfW, baseY)
        close()
    }

internal fun DrawScope.drawStar(center: Offset, outerRadius: Float, color: Color) {
    val innerRadius = outerRadius * 0.42f
    val path = Path()
    for (i in 0 until 10) {
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val angle = Math.toRadians((i * 36 - 90).toDouble())
        val x = center.x + (radius * Math.cos(angle)).toFloat()
        val y = center.y + (radius * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path = path, color = color)
}