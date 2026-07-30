package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.cos
import kotlin.math.sin

internal fun DrawScope.drawCompassBadge(center: Offset, r: Float) {
    drawCircle(
        color = Color(0x40000000),
        radius = r * 0.92f,
        center = Offset(center.x + r * 0.05f, center.y + r * 0.08f),
    )
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(goldMetal.light, goldMetal.mid, goldMetal.dark),
            start = Offset(center.x - r, center.y - r),
            end = Offset(center.x + r, center.y + r),
        ),
        radius = r * 0.92f,
        center = center,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFDFBF2), Color(0xFFE8E2CE)),
            center = Offset(center.x - r * 0.2f, center.y - r * 0.2f),
            radius = r,
        ),
        radius = r * 0.74f,
        center = center,
    )
    drawCircle(
        color = goldMetal.outline,
        radius = r * 0.74f,
        center = center,
        style = Stroke(width = r * 0.04f),
    )

    repeat(8) { i ->
        val angle = (i * 45f) * (Math.PI / 180f).toFloat()
        val isMajor = i % 2 == 0
        val tickOuter = r * 0.66f
        val tickInner = if (isMajor) r * 0.52f else r * 0.58f
        drawLine(
            color = Color(0xFF4A4438).copy(alpha = if (isMajor) 0.9f else 0.5f),
            start = Offset(center.x + tickInner * cos(angle), center.y + tickInner * sin(angle)),
            end = Offset(center.x + tickOuter * cos(angle), center.y + tickOuter * sin(angle)),
            strokeWidth = r * (if (isMajor) 0.05f else 0.03f),
            cap = StrokeCap.Round,
        )
    }

    rotate(degrees = 38f, pivot = center) {
        drawPath(
            path = Path().apply {
                moveTo(center.x, center.y - r * 0.55f)
                lineTo(center.x + r * 0.13f, center.y)
                lineTo(center.x - r * 0.13f, center.y)
                close()
            },
            color = Color(0xFFE0356B),
        )
        drawPath(
            path = Path().apply {
                moveTo(center.x, center.y + r * 0.55f)
                lineTo(center.x + r * 0.13f, center.y)
                lineTo(center.x - r * 0.13f, center.y)
                close()
            },
            color = Color(0xFFF4F7FB),
        )
    }
    drawCircle(color = goldMetal.dark, radius = r * 0.08f, center = center)
    drawCircle(color = goldMetal.light, radius = r * 0.045f, center = center)

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
            center = Offset(center.x - r * 0.3f, center.y - r * 0.35f),
            radius = r * 0.5f,
        ),
        radius = r * 0.74f,
        center = center,
    )
}

internal fun DrawScope.drawCheckBadge(center: Offset, r: Float) {
    val path = Path().apply {
        moveTo(center.x - r * 0.62f, center.y + r * 0.02f)
        lineTo(center.x - r * 0.14f, center.y + r * 0.5f)
        lineTo(center.x + r * 0.68f, center.y - r * 0.52f)
    }

    translate(left = r * 0.06f, top = r * 0.09f) {
        drawPath(
            path = path,
            color = Color(0x40000000),
            style = Stroke(width = r * 0.34f, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
    drawPath(
        path = path,
        color = Color(0xFF0E6B30),
        style = Stroke(width = r * 0.36f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFDFFFD0), Color(0xFF57D95F), Color(0xFF1F9E4C)),
            start = Offset(center.x, center.y - r * 0.5f),
            end = Offset(center.x, center.y + r * 0.5f),
        ),
        style = Stroke(width = r * 0.30f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.55f, center.y - r * 0.02f)
            lineTo(center.x - r * 0.16f, center.y + r * 0.36f)
        },
        color = Color.White.copy(alpha = 0.55f),
        style = Stroke(width = r * 0.09f, cap = StrokeCap.Round),
    )
}

internal fun DrawScope.drawTargetBadge(center: Offset, r: Float) {
    drawCircle(
        color = Color(0x40000000),
        radius = r * 0.92f,
        center = Offset(center.x + r * 0.05f, center.y + r * 0.08f),
    )

    val rings = listOf(
        r * 0.92f to Color(0xFFE8352E),
        r * 0.72f to Color(0xFFF4F7FB),
        r * 0.52f to Color(0xFFE8352E),
        r * 0.32f to Color(0xFFF4F7FB),
        r * 0.14f to Color(0xFFE8352E),
    )
    rings.forEach { (radius, color) ->
        drawCircle(color = color, radius = radius, center = center)
    }
    drawCircle(
        color = Color(0xFF8E1B14),
        radius = r * 0.92f,
        center = center,
        style = Stroke(width = r * 0.04f),
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.4f), Color.Transparent),
            center = Offset(center.x - r * 0.3f, center.y - r * 0.35f),
            radius = r * 0.6f,
        ),
        radius = r * 0.92f,
        center = center,
    )

    val dartAngle = 40f
    rotate(degrees = dartAngle, pivot = center) {
        drawLine(
            color = Color(0xFF3C4654),
            start = Offset(center.x, center.y - r * 1.05f),
            end = center,
            strokeWidth = r * 0.09f,
            cap = StrokeCap.Round,
        )
        listOf(-1f, 1f).forEach { sign ->
            drawPath(
                path = Path().apply {
                    moveTo(center.x, center.y - r * 1.02f)
                    lineTo(center.x + sign * r * 0.22f, center.y - r * 1.22f)
                    lineTo(center.x + sign * r * 0.22f, center.y - r * 0.95f)
                    close()
                },
                color = Color(0xFF2E9BFF),
            )
        }
        drawCircle(color = Color(0xFFFFD93C), radius = r * 0.07f, center = center)
    }
}

internal data class GemPaletteColors(
    val top: Color,
    val light: Color,
    val mid: Color,
    val deep: Color,
    val outline: Color,
)

internal val gemBlue = GemPaletteColors(
    top = Color(0xFFDFFCFF),
    light = Color(0xFF9BE8FF),
    mid = Color(0xFF3FA9F5),
    deep = Color(0xFF1666D8),
    outline = Color(0xFF0D3E9E),
)

internal val gemPink = GemPaletteColors(
    top = Color(0xFFFFEBF7),
    light = Color(0xFFFFB0DE),
    mid = Color(0xFFF55FB8),
    deep = Color(0xFFC2187F),
    outline = Color(0xFF7C0E52),
)

internal fun DrawScope.drawGemColored(center: Offset, r: Float, gem: GemPaletteColors) {
    val crownY = center.y - r * 0.42f
    val girdleY = center.y - r * 0.02f
    val tipY = center.y + r * 0.92f
    val halfW = r * 0.96f

    translate(left = r * 0.05f, top = r * 0.10f) {
        drawPath(
            path = gemOutline(center, crownY, girdleY, tipY, halfW),
            color = Color(0x33000000),
        )
    }

    drawPath(
        path = gemOutline(center, crownY, girdleY, tipY, halfW),
        brush = Brush.linearGradient(
            colors = listOf(gem.light, gem.mid, gem.deep),
            start = Offset(center.x, crownY),
            end = Offset(center.x, tipY),
        ),
    )

    val tableHalf = halfW * 0.44f
    drawPath(
        path = Path().apply {
            moveTo(center.x - tableHalf, crownY)
            lineTo(center.x + tableHalf, crownY)
            lineTo(center.x + halfW * 0.62f, girdleY)
            lineTo(center.x - halfW * 0.62f, girdleY)
            close()
        },
        color = gem.top.copy(alpha = 0.9f),
    )

    listOf(-1f, 1f).forEach { sign ->
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * tableHalf, crownY)
                lineTo(center.x + sign * halfW, girdleY)
                lineTo(center.x + sign * halfW * 0.62f, girdleY)
                close()
            },
            color = if (sign < 0) gem.light.copy(alpha = 0.85f) else gem.mid.copy(alpha = 0.9f),
        )
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * halfW * 0.62f, girdleY)
                lineTo(center.x + sign * halfW * 0.20f, girdleY)
                lineTo(center.x, tipY)
                close()
            },
            color = if (sign < 0) gem.mid.copy(alpha = 0.75f) else gem.deep.copy(alpha = 0.85f),
        )
    }

    drawPath(
        path = gemOutline(center, crownY, girdleY, tipY, halfW),
        color = gem.outline,
        style = Stroke(width = r * 0.05f),
    )

    drawFourPointSparkle(
        center = Offset(center.x - halfW * 0.30f, crownY + r * 0.12f),
        size = r * 0.20f,
        color = Color.White,
    )
}

internal fun DrawScope.drawGlobeBadge(center: Offset, r: Float) {
    drawCircle(
        color = Color(0x40000000),
        radius = r * 0.88f,
        center = Offset(center.x + r * 0.05f, center.y + r * 0.08f),
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFF9BE8FF), Color(0xFF2E9BFF), Color(0xFF0E4C9E)),
            center = Offset(center.x - r * 0.25f, center.y - r * 0.25f),
            radius = r * 1.2f,
        ),
        radius = r * 0.88f,
        center = center,
    )

    val land = listOf(
        Path().apply {
            moveTo(center.x - r * 0.55f, center.y - r * 0.35f)
            cubicTo(
                center.x - r * 0.25f, center.y - r * 0.62f,
                center.x + r * 0.05f, center.y - r * 0.42f,
                center.x - r * 0.08f, center.y - r * 0.18f,
            )
            cubicTo(
                center.x - r * 0.32f, center.y - r * 0.05f,
                center.x - r * 0.6f, center.y - r * 0.12f,
                center.x - r * 0.55f, center.y - r * 0.35f,
            )
            close()
        },
        Path().apply {
            moveTo(center.x + r * 0.18f, center.y + r * 0.05f)
            cubicTo(
                center.x + r * 0.52f, center.y - r * 0.12f,
                center.x + r * 0.68f, center.y + r * 0.22f,
                center.x + r * 0.42f, center.y + r * 0.45f,
            )
            cubicTo(
                center.x + r * 0.18f, center.y + r * 0.55f,
                center.x + r * 0.05f, center.y + r * 0.25f,
                center.x + r * 0.18f, center.y + r * 0.05f,
            )
            close()
        },
        Path().apply {
            moveTo(center.x - r * 0.35f, center.y + r * 0.35f)
            cubicTo(
                center.x - r * 0.15f, center.y + r * 0.28f,
                center.x - r * 0.08f, center.y + r * 0.5f,
                center.x - r * 0.28f, center.y + r * 0.6f,
            )
            cubicTo(
                center.x - r * 0.48f, center.y + r * 0.62f,
                center.x - r * 0.5f, center.y + r * 0.42f,
                center.x - r * 0.35f, center.y + r * 0.35f,
            )
            close()
        },
    )
    land.forEach { drawPath(path = it, color = Color(0xFF57D95F)) }

    drawOval(
        color = Color.White.copy(alpha = 0.35f),
        topLeft = Offset(center.x - r * 0.88f, center.y - r * 0.30f),
        size = Size(r * 1.76f, r * 0.60f),
        style = Stroke(width = r * 0.035f),
    )
    drawLine(
        color = Color.White.copy(alpha = 0.35f),
        start = Offset(center.x, center.y - r * 0.88f),
        end = Offset(center.x, center.y + r * 0.88f),
        strokeWidth = r * 0.035f,
    )
    drawOval(
        color = Color.White.copy(alpha = 0.3f),
        topLeft = Offset(center.x - r * 0.45f, center.y - r * 0.88f),
        size = Size(r * 0.9f, r * 1.76f),
        style = Stroke(width = r * 0.03f),
    )

    drawCircle(
        color = Color(0xFF0D3E9E),
        radius = r * 0.88f,
        center = center,
        style = Stroke(width = r * 0.05f),
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
            center = Offset(center.x - r * 0.35f, center.y - r * 0.4f),
            radius = r * 0.55f,
        ),
        radius = r * 0.88f,
        center = center,
    )
}

internal fun DrawScope.drawOwlBadge(center: Offset, r: Float) {
    val bodyBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFB89A6E), Color(0xFF6E4E2A)),
        start = Offset(center.x, center.y - r),
        end = Offset(center.x, center.y + r),
    )

    listOf(-1f, 1f).forEach { sign ->
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * r * 0.42f, center.y - r * 0.95f)
                lineTo(center.x + sign * r * 0.72f, center.y - r * 0.45f)
                lineTo(center.x + sign * r * 0.22f, center.y - r * 0.62f)
                close()
            },
            color = Color(0xFF6E4E2A),
        )
    }

    drawOval(
        brush = bodyBrush,
        topLeft = Offset(center.x - r * 0.78f, center.y - r * 0.72f),
        size = Size(r * 1.56f, r * 1.72f),
    )
    drawOval(
        color = Color(0xFF4A3520),
        topLeft = Offset(center.x - r * 0.78f, center.y - r * 0.72f),
        size = Size(r * 1.56f, r * 1.72f),
        style = Stroke(width = r * 0.05f),
    )

    drawOval(
        color = Color(0xFFE8D5B0),
        topLeft = Offset(center.x - r * 0.48f, center.y + r * 0.02f),
        size = Size(r * 0.96f, r * 0.88f),
    )
    listOf(-0.28f, 0f, 0.28f).forEach { dx ->
        repeat(2) { row ->
            drawPath(
                path = Path().apply {
                    val fx = center.x + dx * r + (if (row % 2 == 0) 0f else r * 0.14f)
                    val fy = center.y + r * 0.18f + row * r * 0.26f
                    moveTo(fx - r * 0.09f, fy)
                    quadraticTo(fx, fy + r * 0.18f, fx + r * 0.09f, fy)
                },
                color = Color(0xFFB89A6E),
                style = Stroke(width = r * 0.035f, cap = StrokeCap.Round),
            )
        }
    }

    listOf(-1f, 1f).forEach { sign ->
        val eyeCenter = Offset(center.x + sign * r * 0.34f, center.y - r * 0.28f)
        drawCircle(color = Color(0xFFF6EED8), radius = r * 0.30f, center = eyeCenter)
        drawCircle(color = Color(0xFF4A3520), radius = r * 0.30f, center = eyeCenter, style = Stroke(width = r * 0.035f))
        drawCircle(color = Color(0xFF2A1C0E), radius = r * 0.14f, center = eyeCenter)
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = r * 0.05f,
            center = Offset(eyeCenter.x - r * 0.05f, eyeCenter.y - r * 0.05f),
        )
    }

    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.09f, center.y - r * 0.08f)
            lineTo(center.x + r * 0.09f, center.y - r * 0.08f)
            lineTo(center.x, center.y + r * 0.14f)
            close()
        },
        color = Color(0xFFFF9E1B),
    )
}

internal fun DrawScope.drawCrackedShieldBadge(center: Offset, r: Float) {
    val shield = Path().apply {
        moveTo(center.x, center.y - r * 0.95f)
        cubicTo(
            center.x + r * 0.55f, center.y - r * 0.78f,
            center.x + r * 0.85f, center.y - r * 0.72f,
            center.x + r * 0.85f, center.y - r * 0.35f,
        )
        cubicTo(
            center.x + r * 0.85f, center.y + r * 0.35f,
            center.x + r * 0.45f, center.y + r * 0.75f,
            center.x, center.y + r * 0.98f,
        )
        cubicTo(
            center.x - r * 0.45f, center.y + r * 0.75f,
            center.x - r * 0.85f, center.y + r * 0.35f,
            center.x - r * 0.85f, center.y - r * 0.35f,
        )
        cubicTo(
            center.x - r * 0.85f, center.y - r * 0.72f,
            center.x - r * 0.55f, center.y - r * 0.78f,
            center.x, center.y - r * 0.95f,
        )
        close()
    }

    translate(left = r * 0.05f, top = r * 0.09f) {
        drawPath(path = shield, color = Color(0x40000000))
    }
    drawPath(
        path = shield,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFF8A5A), Color(0xFFD62839), Color(0xFF7C0E2E)),
            start = Offset(center.x, center.y - r),
            end = Offset(center.x, center.y + r),
        ),
    )
    drawPath(path = shield, color = Color(0xFF4A0818), style = Stroke(width = r * 0.06f))

    drawPath(
        path = Path().apply {
            moveTo(center.x + r * 0.05f, center.y - r * 0.95f)
            lineTo(center.x - r * 0.14f, center.y - r * 0.42f)
            lineTo(center.x + r * 0.12f, center.y - r * 0.18f)
            lineTo(center.x - r * 0.1f, center.y + r * 0.22f)
            lineTo(center.x + r * 0.14f, center.y + r * 0.48f)
            lineTo(center.x + r * 0.02f, center.y + r * 0.97f)
        },
        color = Color(0xFF2A0510),
        style = Stroke(width = r * 0.07f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )

    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.55f, center.y - r * 0.55f)
            cubicTo(
                center.x - r * 0.62f, center.y - r * 0.2f,
                center.x - r * 0.5f, center.y + r * 0.15f,
                center.x - r * 0.32f, center.y + r * 0.42f,
            )
        },
        color = Color.White.copy(alpha = 0.35f),
        style = Stroke(width = r * 0.09f, cap = StrokeCap.Round),
    )
}

internal fun DrawScope.drawGemBadge(center: Offset, r: Float) {
    drawGemColored(center, r, gemBlue)
}

private fun gemOutline(center: Offset, crownY: Float, girdleY: Float, tipY: Float, halfW: Float): Path =
    Path().apply {
        moveTo(center.x - halfW * 0.44f, crownY)
        lineTo(center.x + halfW * 0.44f, crownY)
        lineTo(center.x + halfW, girdleY)
        lineTo(center.x, tipY)
        lineTo(center.x - halfW, girdleY)
        close()
    }