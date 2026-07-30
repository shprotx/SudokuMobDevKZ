package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

internal fun DrawScope.drawLeafBranch(center: Offset, r: Float) {
    val stemColor = Color(0xFF2E6B1E)
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.15f, center.y + r * 0.95f)
            cubicTo(
                center.x + r * 0.05f, center.y + r * 0.3f,
                center.x + r * 0.1f, center.y - r * 0.3f,
                center.x + r * 0.05f, center.y - r * 0.95f,
            )
        },
        color = stemColor,
        style = Stroke(width = r * 0.09f, cap = StrokeCap.Round),
    )

    val leaves = listOf(
        Triple(Offset(center.x - r * 0.12f, center.y + r * 0.55f), -55f, 0.42f),
        Triple(Offset(center.x + r * 0.06f, center.y + r * 0.25f), 55f, 0.46f),
        Triple(Offset(center.x - r * 0.02f, center.y - r * 0.08f), -50f, 0.5f),
        Triple(Offset(center.x + r * 0.10f, center.y - r * 0.42f), 50f, 0.46f),
        Triple(Offset(center.x + r * 0.05f, center.y - r * 0.85f), 0f, 0.44f),
    )
    leaves.forEach { (pos, angle, sizeFactor) ->
        drawSingleLeaf(pos, r * sizeFactor, angle)
    }
}

private fun DrawScope.drawSingleLeaf(base: Offset, len: Float, angleDeg: Float) {
    rotate(degrees = angleDeg, pivot = base) {
        val path = Path().apply {
            moveTo(base.x, base.y)
            cubicTo(
                base.x + len * 0.45f, base.y - len * 0.35f,
                base.x + len * 0.45f, base.y - len * 0.8f,
                base.x, base.y - len,
            )
            cubicTo(
                base.x - len * 0.45f, base.y - len * 0.8f,
                base.x - len * 0.45f, base.y - len * 0.35f,
                base.x, base.y,
            )
            close()
        }
        drawPath(
            path = path,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFA8E66E), Color(0xFF3D9C34)),
                start = Offset(base.x, base.y - len),
                end = Offset(base.x, base.y),
            ),
        )
        drawLine(
            color = Color(0xFF2E6B1E).copy(alpha = 0.7f),
            start = base,
            end = Offset(base.x, base.y - len * 0.85f),
            strokeWidth = len * 0.05f,
        )
    }
}

internal fun DrawScope.drawSunBadge(center: Offset, r: Float) {
    repeat(10) { i ->
        val angle = (i * 36f) * (Math.PI / 180f).toFloat()
        val rayBase = r * 0.62f
        val rayTip = r * 1.05f
        val halfSpread = 8f * (Math.PI / 180f).toFloat()
        drawPath(
            path = Path().apply {
                moveTo(
                    center.x + rayBase * cos(angle - halfSpread),
                    center.y + rayBase * sin(angle - halfSpread),
                )
                lineTo(center.x + rayTip * cos(angle), center.y + rayTip * sin(angle))
                lineTo(
                    center.x + rayBase * cos(angle + halfSpread),
                    center.y + rayBase * sin(angle + halfSpread),
                )
                close()
            },
            color = if (i % 2 == 0) Color(0xFFFFC93C) else Color(0xFFFF9E1B),
        )
    }

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFFF6B0), Color(0xFFFFC93C), Color(0xFFFF8A00)),
            center = Offset(center.x - r * 0.15f, center.y - r * 0.15f),
            radius = r * 0.85f,
        ),
        radius = r * 0.60f,
        center = center,
    )
    drawCircle(
        color = Color(0xFFB25A00),
        radius = r * 0.60f,
        center = center,
        style = Stroke(width = r * 0.045f),
    )
    drawCircle(
        color = Color.White.copy(alpha = 0.6f),
        radius = r * 0.14f,
        center = Offset(center.x - r * 0.22f, center.y - r * 0.24f),
    )
}

internal fun DrawScope.drawMountainBadge(center: Offset, r: Float) {
    val baseY = center.y + r * 0.75f
    val backPeak = Path().apply {
        moveTo(center.x - r * 0.15f, baseY)
        lineTo(center.x + r * 0.42f, center.y - r * 0.72f)
        lineTo(center.x + r * 1.0f, baseY)
        close()
    }
    drawPath(
        path = backPeak,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF7E90C8), Color(0xFF3C3F78)),
            start = Offset(center.x, center.y - r * 0.72f),
            end = Offset(center.x, baseY),
        ),
    )
    drawSnowCap(Offset(center.x + r * 0.42f, center.y - r * 0.72f), r * 0.34f, tilt = 0.06f)

    val frontPeak = Path().apply {
        moveTo(center.x - r * 1.02f, baseY)
        lineTo(center.x - r * 0.22f, center.y - r * 0.98f)
        lineTo(center.x + r * 0.58f, baseY)
        close()
    }
    drawPath(
        path = frontPeak,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF9FB6E8), Color(0xFF4A4F94)),
            start = Offset(center.x - r * 0.22f, center.y - r * 0.98f),
            end = Offset(center.x, baseY),
        ),
    )
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.22f, center.y - r * 0.98f)
            lineTo(center.x + r * 0.10f, baseY)
            lineTo(center.x + r * 0.58f, baseY)
            close()
        },
        color = Color(0xFF353A6E).copy(alpha = 0.45f),
    )
    drawSnowCap(Offset(center.x - r * 0.22f, center.y - r * 0.98f), r * 0.42f, tilt = -0.05f)
}

private fun DrawScope.drawSnowCap(peak: Offset, width: Float, tilt: Float) {
    drawPath(
        path = Path().apply {
            moveTo(peak.x, peak.y)
            lineTo(peak.x + width * 0.55f, peak.y + width * 0.62f)
            lineTo(peak.x + width * 0.3f, peak.y + width * 0.52f)
            lineTo(peak.x + width * 0.12f, peak.y + width * 0.72f)
            lineTo(peak.x - width * 0.1f, peak.y + width * 0.5f)
            lineTo(peak.x - width * 0.32f, peak.y + width * 0.68f)
            lineTo(peak.x - width * (0.55f + tilt), peak.y + width * 0.58f)
            close()
        },
        color = Color(0xFFF4F9FF),
    )
}

internal fun DrawScope.drawMoonBadge(center: Offset, r: Float) {
    val moonPath = Path().apply {
        addOval(
            Rect(
                center.x - r * 0.85f, center.y - r * 0.85f,
                center.x + r * 0.85f, center.y + r * 0.85f,
            )
        )
    }
    val cutPath = Path().apply {
        addOval(
            Rect(
                center.x - r * 0.45f, center.y - r * 1.15f,
                center.x + r * 1.25f, center.y + r * 0.55f,
            )
        )
    }
    val crescent = Path().apply { op(moonPath, cutPath, PathOperation.Difference) }

    drawPath(
        path = crescent,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFF6C8), Color(0xFFF5D76A)),
            start = Offset(center.x - r, center.y - r),
            end = Offset(center.x + r * 0.4f, center.y + r),
        ),
    )
    drawPath(
        path = crescent,
        color = Color(0xFFC9A63C),
        style = Stroke(width = r * 0.05f),
    )

    val craters = listOf(
        Triple(-0.45f, 0.15f, 0.14f),
        Triple(-0.15f, 0.55f, 0.10f),
        Triple(-0.55f, -0.30f, 0.09f),
    )
    craters.forEach { (dx, dy, sizeFactor) ->
        drawCircle(
            color = Color(0xFFD9B94E),
            radius = r * sizeFactor,
            center = Offset(center.x + dx * r, center.y + dy * r),
        )
        drawCircle(
            color = Color(0xFFB8983A).copy(alpha = 0.6f),
            radius = r * sizeFactor,
            center = Offset(center.x + dx * r, center.y + dy * r),
            style = Stroke(width = r * 0.02f),
        )
    }
}

internal fun DrawScope.drawSunriseBadge(center: Offset, r: Float) {
    val horizonY = center.y + r * 0.25f

    repeat(7) { i ->
        val angle = (180f + i * 30f) * (Math.PI / 180f).toFloat()
        val rayBase = r * 0.52f
        val rayTip = r * 0.95f
        drawLine(
            color = Color(0xFFFFD24A),
            start = Offset(center.x + rayBase * cos(angle), horizonY + rayBase * sin(angle)),
            end = Offset(center.x + rayTip * cos(angle), horizonY + rayTip * sin(angle)),
            strokeWidth = r * 0.07f,
            cap = StrokeCap.Round,
        )
    }

    val sunPath = Path().apply {
        moveTo(center.x - r * 0.45f, horizonY)
        arcTo(
            rect = Rect(
                center.x - r * 0.45f, horizonY - r * 0.45f,
                center.x + r * 0.45f, horizonY + r * 0.45f,
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 180f,
            forceMoveTo = false,
        )
        close()
    }
    drawPath(
        path = sunPath,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFFF6B0), Color(0xFFFF8A3C)),
            start = Offset(center.x, horizonY - r * 0.45f),
            end = Offset(center.x, horizonY),
        ),
    )

    drawRect(
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF2E6FA8), Color(0xFF14395E)),
            start = Offset(center.x, horizonY),
            end = Offset(center.x, horizonY + r * 0.75f),
        ),
        topLeft = Offset(center.x - r * 1.05f, horizonY),
        size = Size(r * 2.1f, r * 0.75f),
    )

    listOf(0.16f, 0.34f, 0.52f).forEachIndexed { index, dy ->
        drawLine(
            color = Color(0xFFFFC93C).copy(alpha = 0.75f - index * 0.2f),
            start = Offset(center.x - r * (0.45f - index * 0.12f), horizonY + dy * r),
            end = Offset(center.x + r * (0.45f - index * 0.12f), horizonY + dy * r),
            strokeWidth = r * (0.09f - index * 0.02f),
            cap = StrokeCap.Round,
        )
    }
}