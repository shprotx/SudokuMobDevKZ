package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import kotlin.math.cos
import kotlin.math.sin

private object CalendarPalette {
    val paperTop = Color(0xFFFFFFFF)
    val paperBottom = Color(0xFFE4E9F2)
    val headerLight = Color(0xFFFF6A5A)
    val headerDark = Color(0xFFD62839)
    val outline = Color(0xFF6E7480)
    val ringMetal = Color(0xFF9AA4B2)
}

private fun DrawScope.drawCalendarBody(center: Offset, r: Float): Offset {
    val halfW = r * 0.92f
    val top = center.y - r * 0.72f
    val bottom = center.y + r * 0.88f
    val corner = r * 0.16f

    translate(left = r * 0.06f, top = r * 0.09f) {
        drawRoundRect(
            color = Color(0x40000000),
            topLeft = Offset(center.x - halfW, top),
            size = Size(halfW * 2f, bottom - top),
            cornerRadius = CornerRadius(corner, corner),
        )
    }

    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(CalendarPalette.paperTop, CalendarPalette.paperBottom),
            start = Offset(center.x, top),
            end = Offset(center.x, bottom),
        ),
        topLeft = Offset(center.x - halfW, top),
        size = Size(halfW * 2f, bottom - top),
        cornerRadius = CornerRadius(corner, corner),
    )

    val headerHeight = r * 0.46f
    drawPath(
        path = Path().apply {
            moveTo(center.x - halfW, top + corner)
            quadraticTo(center.x - halfW, top, center.x - halfW + corner, top)
            lineTo(center.x + halfW - corner, top)
            quadraticTo(center.x + halfW, top, center.x + halfW, top + corner)
            lineTo(center.x + halfW, top + headerHeight)
            lineTo(center.x - halfW, top + headerHeight)
            close()
        },
        brush = Brush.linearGradient(
            colors = listOf(CalendarPalette.headerLight, CalendarPalette.headerDark),
            start = Offset(center.x, top),
            end = Offset(center.x, top + headerHeight),
        ),
    )

    listOf(-0.5f, 0.5f).forEach { dx ->
        val ringX = center.x + dx * halfW * 1.1f
        drawLine(
            color = CalendarPalette.ringMetal,
            start = Offset(ringX, top - r * 0.18f),
            end = Offset(ringX, top + r * 0.06f),
            strokeWidth = r * 0.09f,
            cap = StrokeCap.Round,
        )
        drawCircle(
            color = CalendarPalette.headerDark.copy(alpha = 0.6f),
            radius = r * 0.055f,
            center = Offset(ringX, top + r * 0.05f),
        )
    }

    drawRoundRect(
        color = CalendarPalette.outline,
        topLeft = Offset(center.x - halfW, top),
        size = Size(halfW * 2f, bottom - top),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(width = r * 0.05f),
    )

    return Offset(center.x, top + headerHeight + (bottom - top - headerHeight) / 2f)
}

internal fun DrawScope.drawCalendarCheckBadge(center: Offset, r: Float) {
    val contentCenter = drawCalendarBody(center, r)
    val checkPath = Path().apply {
        moveTo(contentCenter.x - r * 0.34f, contentCenter.y)
        lineTo(contentCenter.x - r * 0.08f, contentCenter.y + r * 0.26f)
        lineTo(contentCenter.x + r * 0.38f, contentCenter.y - r * 0.28f)
    }
    drawPath(
        path = checkPath,
        color = Color(0xFF1F9E4C),
        style = Stroke(width = r * 0.16f, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}

internal fun DrawScope.drawCalendarWeekBadge(center: Offset, r: Float) {
    val contentCenter = drawCalendarBody(center, r)
    val cellSize = r * 0.20f
    val gap = r * 0.06f
    val totalW = 4 * cellSize + 3 * gap
    val startX = contentCenter.x - totalW / 2f

    repeat(4) { i ->
        drawRoundRect(
            color = if (i < 3) Color(0xFF57D95F) else Color(0xFFCBD3DE),
            topLeft = Offset(startX + i * (cellSize + gap), contentCenter.y - cellSize),
            size = Size(cellSize, cellSize),
            cornerRadius = CornerRadius(r * 0.04f, r * 0.04f),
        )
    }
    repeat(3) { i ->
        drawRoundRect(
            color = if (i < 2) Color(0xFF57D95F) else Color(0xFFCBD3DE),
            topLeft = Offset(
                startX + (cellSize + gap) / 2f + i * (cellSize + gap),
                contentCenter.y + gap,
            ),
            size = Size(cellSize, cellSize),
            cornerRadius = CornerRadius(r * 0.04f, r * 0.04f),
        )
    }
}

internal fun DrawScope.drawCalendarMonthBadge(center: Offset, r: Float) {
    val contentCenter = drawCalendarBody(center, r)
    val cellSize = r * 0.13f
    val gap = r * 0.05f
    val cols = 5
    val rows = 4
    val totalW = cols * cellSize + (cols - 1) * gap
    val totalH = rows * cellSize + (rows - 1) * gap
    val startX = contentCenter.x - totalW / 2f
    val startY = contentCenter.y - totalH / 2f

    repeat(rows) { row ->
        repeat(cols) { col ->
            val index = row * cols + col
            drawRoundRect(
                color = if (index < 17) Color(0xFFB08CFF) else Color(0xFFCBD3DE),
                topLeft = Offset(startX + col * (cellSize + gap), startY + row * (cellSize + gap)),
                size = Size(cellSize, cellSize),
                cornerRadius = CornerRadius(r * 0.03f, r * 0.03f),
            )
        }
    }
    drawFourPointSparkle(
        center = Offset(startX + 2 * (cellSize + gap) + cellSize / 2f, startY + 3 * (cellSize + gap) + cellSize / 2f),
        size = r * 0.14f,
        color = Color(0xFF7A1AB3),
    )
}

internal fun DrawScope.drawCalendarFortnightBadge(center: Offset, r: Float) {
    val contentCenter = drawCalendarBody(center, r)
    val cellSize = r * 0.155f
    val gap = r * 0.05f
    val cols = 7
    val totalW = cols * cellSize + (cols - 1) * gap
    val startX = contentCenter.x - totalW / 2f

    repeat(2) { row ->
        repeat(cols) { col ->
            val index = row * cols + col
            drawRoundRect(
                color = if (index < 14) Color(0xFF2BB36C) else Color(0xFFCBD3DE),
                topLeft = Offset(
                    startX + col * (cellSize + gap),
                    contentCenter.y - cellSize - gap / 2f + row * (cellSize + gap),
                ),
                size = Size(cellSize, cellSize),
                cornerRadius = CornerRadius(r * 0.035f, r * 0.035f),
            )
        }
    }
}

internal fun DrawScope.drawCalendarStackBadge(center: Offset, r: Float) {
    val sheetW = r * 1.5f
    val sheetH = r * 1.1f
    val corner = r * 0.12f

    listOf(
        Offset(center.x - sheetW / 2f + r * 0.16f, center.y - sheetH / 2f - r * 0.28f) to 0.55f,
        Offset(center.x - sheetW / 2f + r * 0.08f, center.y - sheetH / 2f - r * 0.14f) to 0.75f,
    ).forEach { (topLeft, alpha) ->
        drawRoundRect(
            color = Color(0xFFE4E9F2).copy(alpha = alpha),
            topLeft = topLeft,
            size = Size(sheetW, sheetH),
            cornerRadius = CornerRadius(corner, corner),
        )
        drawRoundRect(
            color = CalendarPalette.outline.copy(alpha = alpha),
            topLeft = topLeft,
            size = Size(sheetW, sheetH),
            cornerRadius = CornerRadius(corner, corner),
            style = Stroke(width = r * 0.035f),
        )
    }

    val frontTopLeft = Offset(center.x - sheetW / 2f, center.y - sheetH / 2f)
    translate(left = r * 0.05f, top = r * 0.07f) {
        drawRoundRect(
            color = Color(0x40000000),
            topLeft = frontTopLeft,
            size = Size(sheetW, sheetH),
            cornerRadius = CornerRadius(corner, corner),
        )
    }
    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(CalendarPalette.paperTop, CalendarPalette.paperBottom),
            start = frontTopLeft,
            end = Offset(frontTopLeft.x, frontTopLeft.y + sheetH),
        ),
        topLeft = frontTopLeft,
        size = Size(sheetW, sheetH),
        cornerRadius = CornerRadius(corner, corner),
    )
    drawPath(
        path = Path().apply {
            moveTo(frontTopLeft.x, frontTopLeft.y + corner)
            quadraticTo(frontTopLeft.x, frontTopLeft.y, frontTopLeft.x + corner, frontTopLeft.y)
            lineTo(frontTopLeft.x + sheetW - corner, frontTopLeft.y)
            quadraticTo(frontTopLeft.x + sheetW, frontTopLeft.y, frontTopLeft.x + sheetW, frontTopLeft.y + corner)
            lineTo(frontTopLeft.x + sheetW, frontTopLeft.y + sheetH * 0.34f)
            lineTo(frontTopLeft.x, frontTopLeft.y + sheetH * 0.34f)
            close()
        },
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFF4A9EFF), Color(0xFF1E3E8F)),
            start = frontTopLeft,
            end = Offset(frontTopLeft.x, frontTopLeft.y + sheetH * 0.34f),
        ),
    )
    drawRoundRect(
        color = CalendarPalette.outline,
        topLeft = frontTopLeft,
        size = Size(sheetW, sheetH),
        cornerRadius = CornerRadius(corner, corner),
        style = Stroke(width = r * 0.045f),
    )

    val num = Offset(center.x, frontTopLeft.y + sheetH * 0.67f)
    drawLine(
        color = CalendarPalette.outline,
        start = Offset(num.x - r * 0.06f, num.y - r * 0.22f),
        end = Offset(num.x - r * 0.06f, num.y + r * 0.22f),
        strokeWidth = r * 0.09f,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = CalendarPalette.outline,
        start = Offset(num.x - r * 0.06f, num.y - r * 0.22f),
        end = Offset(num.x - r * 0.22f, num.y - r * 0.08f),
        strokeWidth = r * 0.08f,
        cap = StrokeCap.Round,
    )
}

internal fun DrawScope.drawHourglassBadge(center: Offset, r: Float) {
    val glassHalfW = r * 0.58f
    val topY = center.y - r * 0.72f
    val bottomY = center.y + r * 0.72f
    val waistY = center.y

    val frame = listOf(topY - r * 0.14f, bottomY)
    frame.forEach { y ->
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(goldMetal.light, goldMetal.dark),
                start = Offset(center.x, y),
                end = Offset(center.x, y + r * 0.14f),
            ),
            topLeft = Offset(center.x - glassHalfW - r * 0.12f, y),
            size = Size((glassHalfW + r * 0.12f) * 2f, r * 0.14f),
            cornerRadius = CornerRadius(r * 0.05f, r * 0.05f),
        )
    }

    val glass = Path().apply {
        moveTo(center.x - glassHalfW, topY)
        lineTo(center.x + glassHalfW, topY)
        cubicTo(
            center.x + glassHalfW, waistY - r * 0.18f,
            center.x + r * 0.10f, waistY - r * 0.05f,
            center.x + r * 0.07f, waistY,
        )
        cubicTo(
            center.x + r * 0.10f, waistY + r * 0.05f,
            center.x + glassHalfW, waistY + r * 0.18f,
            center.x + glassHalfW, bottomY,
        )
        lineTo(center.x - glassHalfW, bottomY)
        cubicTo(
            center.x - glassHalfW, waistY + r * 0.18f,
            center.x - r * 0.10f, waistY + r * 0.05f,
            center.x - r * 0.07f, waistY,
        )
        cubicTo(
            center.x - r * 0.10f, waistY - r * 0.05f,
            center.x - glassHalfW, waistY - r * 0.18f,
            center.x - glassHalfW, topY,
        )
        close()
    }
    drawPath(path = glass, color = Color(0x33FFFFFF))
    drawPath(path = glass, color = goldMetal.outline, style = Stroke(width = r * 0.045f))

    drawPath(
        path = Path().apply {
            moveTo(center.x - glassHalfW * 0.55f, topY + r * 0.16f)
            lineTo(center.x + glassHalfW * 0.55f, topY + r * 0.16f)
            cubicTo(
                center.x + glassHalfW * 0.4f, waistY - r * 0.16f,
                center.x + r * 0.06f, waistY - r * 0.06f,
                center.x, waistY,
            )
            cubicTo(
                center.x - r * 0.06f, waistY - r * 0.06f,
                center.x - glassHalfW * 0.4f, waistY - r * 0.16f,
                center.x - glassHalfW * 0.55f, topY + r * 0.16f,
            )
            close()
        },
        color = Color(0xFFFFD98A),
    )
    drawPath(
        path = Path().apply {
            moveTo(center.x - glassHalfW * 0.72f, bottomY)
            lineTo(center.x + glassHalfW * 0.72f, bottomY)
            lineTo(center.x + glassHalfW * 0.3f, bottomY - r * 0.22f)
            lineTo(center.x, bottomY - r * 0.30f)
            lineTo(center.x - glassHalfW * 0.3f, bottomY - r * 0.22f)
            close()
        },
        color = Color(0xFFFFD98A),
    )
    drawLine(
        color = Color(0xFFFFD98A),
        start = Offset(center.x, waistY),
        end = Offset(center.x, bottomY - r * 0.28f),
        strokeWidth = r * 0.05f,
    )

    drawPath(
        path = Path().apply {
            moveTo(center.x - glassHalfW * 0.7f, topY + r * 0.1f)
            cubicTo(
                center.x - glassHalfW * 0.75f, waistY - r * 0.25f,
                center.x - glassHalfW * 0.5f, waistY - r * 0.2f,
                center.x - r * 0.14f, waistY - r * 0.06f,
            )
        },
        color = Color.White.copy(alpha = 0.5f),
        style = Stroke(width = r * 0.05f, cap = StrokeCap.Round),
    )
}

internal fun DrawScope.drawStopwatchBadge(center: Offset, r: Float) {
    val dialCenter = Offset(center.x, center.y + r * 0.12f)
    val dialRadius = r * 0.78f

    drawRoundRect(
        brush = Brush.linearGradient(
            colors = listOf(goldMetal.light, goldMetal.dark),
            start = Offset(center.x, center.y - r * 1.05f),
            end = Offset(center.x, center.y - r * 0.75f),
        ),
        topLeft = Offset(center.x - r * 0.12f, center.y - r * 1.05f),
        size = Size(r * 0.24f, r * 0.28f),
        cornerRadius = CornerRadius(r * 0.05f, r * 0.05f),
    )
    listOf(-1f, 1f).forEach { sign ->
        drawLine(
            color = goldMetal.dark,
            start = Offset(center.x + sign * r * 0.45f, center.y - r * 0.72f),
            end = Offset(center.x + sign * r * 0.62f, center.y - r * 0.88f),
            strokeWidth = r * 0.11f,
            cap = StrokeCap.Round,
        )
    }

    drawCircle(
        color = Color(0x40000000),
        radius = dialRadius,
        center = Offset(dialCenter.x + r * 0.05f, dialCenter.y + r * 0.08f),
    )
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(goldMetal.light, goldMetal.mid, goldMetal.dark),
            start = Offset(dialCenter.x - dialRadius, dialCenter.y - dialRadius),
            end = Offset(dialCenter.x + dialRadius, dialCenter.y + dialRadius),
        ),
        radius = dialRadius,
        center = dialCenter,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFDFBF2), Color(0xFFE8E2CE)),
            center = Offset(dialCenter.x - r * 0.2f, dialCenter.y - r * 0.2f),
            radius = dialRadius,
        ),
        radius = dialRadius * 0.78f,
        center = dialCenter,
    )
    drawCircle(
        color = goldMetal.outline,
        radius = dialRadius * 0.78f,
        center = dialCenter,
        style = Stroke(width = r * 0.04f),
    )

    repeat(12) { i ->
        val angle = (i * 30f) * (Math.PI / 180f).toFloat()
        val outer = dialRadius * 0.70f
        val inner = dialRadius * (if (i % 3 == 0) 0.56f else 0.63f)
        drawLine(
            color = Color(0xFF4A4438),
            start = Offset(
                dialCenter.x + inner * cos(angle),
                dialCenter.y + inner * sin(angle),
            ),
            end = Offset(
                dialCenter.x + outer * cos(angle),
                dialCenter.y + outer * sin(angle),
            ),
            strokeWidth = r * (if (i % 3 == 0) 0.045f else 0.028f),
            cap = StrokeCap.Round,
        )
    }

    drawLine(
        color = Color(0xFFE0356B),
        start = dialCenter,
        end = Offset(dialCenter.x + dialRadius * 0.42f, dialCenter.y - dialRadius * 0.48f),
        strokeWidth = r * 0.055f,
        cap = StrokeCap.Round,
    )
    drawCircle(color = goldMetal.outline, radius = r * 0.07f, center = dialCenter)
    drawCircle(color = goldMetal.light, radius = r * 0.04f, center = dialCenter)

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White.copy(alpha = 0.45f), Color.Transparent),
            center = Offset(dialCenter.x - r * 0.3f, dialCenter.y - r * 0.35f),
            radius = r * 0.5f,
        ),
        radius = dialRadius * 0.78f,
        center = dialCenter,
    )
}

internal fun DrawScope.drawYearMedalBadge(center: Offset, r: Float, accent: Color) {
    drawRibbonMedal(
        center = center,
        r = r,
        metal = goldMetal,
        ribbonLight = Color(0xFFFF5A6E),
        ribbonDark = Color(0xFFC81E3C),
        numeral = null,
        withLaurels = true,
        accent = accent,
    )
}