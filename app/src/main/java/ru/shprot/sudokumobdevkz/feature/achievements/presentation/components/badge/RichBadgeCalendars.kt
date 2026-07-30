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