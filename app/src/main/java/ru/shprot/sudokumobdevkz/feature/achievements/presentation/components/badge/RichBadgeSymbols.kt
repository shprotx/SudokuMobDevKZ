package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
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

internal fun DrawScope.drawGemBadge(center: Offset, r: Float) {
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
            colors = listOf(Color(0xFF9BE8FF), Color(0xFF3FA9F5), Color(0xFF1666D8)),
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
        color = Color(0xFFDFFCFF).copy(alpha = 0.9f),
    )

    listOf(-1f, 1f).forEach { sign ->
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * tableHalf, crownY)
                lineTo(center.x + sign * halfW, girdleY)
                lineTo(center.x + sign * halfW * 0.62f, girdleY)
                close()
            },
            color = if (sign < 0) Color(0xFF9BE8FF).copy(alpha = 0.85f) else Color(0xFF3FA9F5).copy(alpha = 0.9f),
        )
        drawPath(
            path = Path().apply {
                moveTo(center.x + sign * halfW * 0.62f, girdleY)
                lineTo(center.x + sign * halfW * 0.20f, girdleY)
                lineTo(center.x, tipY)
                close()
            },
            color = if (sign < 0) Color(0xFF3FA9F5).copy(alpha = 0.75f) else Color(0xFF1666D8).copy(alpha = 0.85f),
        )
    }

    drawPath(
        path = gemOutline(center, crownY, girdleY, tipY, halfW),
        color = Color(0xFF0D3E9E),
        style = Stroke(width = r * 0.05f),
    )

    drawFourPointSparkle(
        center = Offset(center.x - halfW * 0.30f, crownY + r * 0.12f),
        size = r * 0.20f,
        color = Color.White,
    )
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