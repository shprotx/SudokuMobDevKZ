package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate

private object FlamePalette {
    val outer = Color(0xFFE2481B)
    val mid = Color(0xFFFF9A1F)
    val core = Color(0xFFFFE870)
    val coreHot = Color(0xFFFFFBE0)
    val glow = Color(0x66FF7A00)
    val ember = Color(0xFF7A2E10)
}

internal fun DrawScope.drawFireSmall(center: Offset, r: Float) {
    drawEmbers(center, r)
    drawFlameTongue(
        center = Offset(center.x, center.y - r * 0.05f),
        r = r * 0.95f,
        color = FlamePalette.outer,
        tiltFactor = 0.16f,
    )
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.22f),
        r = r * 0.55f,
        color = FlamePalette.core,
        tiltFactor = -0.10f,
    )
}

internal fun DrawScope.drawFireMedium(center: Offset, r: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(FlamePalette.glow, Color.Transparent),
            center = center,
            radius = r * 1.4f,
        ),
        radius = r * 1.4f,
        center = center,
    )
    drawEmbers(center, r)
    drawFlameTongue(center, r * 1.22f, FlamePalette.outer, tiltFactor = 0.22f)
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.16f),
        r = r * 0.88f,
        color = FlamePalette.mid,
        tiltFactor = -0.16f,
    )
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.36f),
        r = r * 0.52f,
        color = FlamePalette.core,
        tiltFactor = 0.10f,
    )
}

internal fun DrawScope.drawFireBig(center: Offset, r: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(FlamePalette.glow, Color.Transparent),
            center = center,
            radius = r * 1.65f,
        ),
        radius = r * 1.65f,
        center = center,
    )

    drawFlameTongue(
        center = Offset(center.x - r * 0.52f, center.y + r * 0.30f),
        r = r * 0.52f,
        color = FlamePalette.outer.copy(alpha = 0.9f),
        tiltFactor = -0.35f,
    )
    drawFlameTongue(
        center = Offset(center.x + r * 0.52f, center.y + r * 0.30f),
        r = r * 0.52f,
        color = FlamePalette.outer.copy(alpha = 0.9f),
        tiltFactor = 0.35f,
    )

    drawFlameTongue(center, r * 1.38f, FlamePalette.outer, tiltFactor = 0.20f)
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.14f),
        r = r * 1.02f,
        color = FlamePalette.mid,
        tiltFactor = -0.18f,
    )
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.34f),
        r = r * 0.62f,
        color = FlamePalette.core,
        tiltFactor = 0.12f,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(FlamePalette.coreHot, FlamePalette.core.copy(alpha = 0f)),
            center = Offset(center.x, center.y + r * 0.44f),
            radius = r * 0.44f,
        ),
        radius = r * 0.44f,
        center = Offset(center.x, center.y + r * 0.44f),
    )

    val sparks = listOf(
        Offset(-0.75f, -0.85f) to 0.07f,
        Offset(0.65f, -1.0f) to 0.055f,
        Offset(0.95f, -0.45f) to 0.045f,
        Offset(-1.0f, -0.25f) to 0.05f,
    )
    sparks.forEach { (rel, sizeFactor) ->
        drawCircle(
            color = FlamePalette.core,
            radius = r * sizeFactor,
            center = Offset(center.x + rel.x * r, center.y + rel.y * r),
        )
    }
}

private fun DrawScope.drawEmbers(center: Offset, r: Float) {
    drawOval(
        color = FlamePalette.ember,
        topLeft = Offset(center.x - r * 0.72f, center.y + r * 0.72f),
        size = Size(r * 1.44f, r * 0.34f),
    )
    drawOval(
        color = FlamePalette.outer.copy(alpha = 0.55f),
        topLeft = Offset(center.x - r * 0.5f, center.y + r * 0.76f),
        size = Size(r, r * 0.22f),
    )
}

private fun DrawScope.drawFlameTongue(center: Offset, r: Float, color: Color, tiltFactor: Float) {
    val tipX = center.x + r * tiltFactor
    val path = Path().apply {
        moveTo(tipX, center.y - r)
        cubicTo(
            tipX + r * 0.10f, center.y - r * 0.55f,
            center.x + r * 0.72f, center.y - r * 0.42f,
            center.x + r * 0.66f, center.y + r * 0.18f,
        )
        cubicTo(
            center.x + r * 0.60f, center.y + r * 0.72f,
            center.x + r * 0.28f, center.y + r * 0.95f,
            center.x, center.y + r * 0.95f,
        )
        cubicTo(
            center.x - r * 0.28f, center.y + r * 0.95f,
            center.x - r * 0.60f, center.y + r * 0.72f,
            center.x - r * 0.66f, center.y + r * 0.18f,
        )
        cubicTo(
            center.x - r * 0.72f, center.y - r * 0.30f,
            tipX - r * 0.30f, center.y - r * 0.50f,
            tipX, center.y - r,
        )
        close()
    }
    drawPath(path = path, color = color)
}

internal fun DrawScope.drawFireBlue(center: Offset, r: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color(0x664AB8FF), Color.Transparent),
            center = center,
            radius = r * 1.65f,
        ),
        radius = r * 1.65f,
        center = center,
    )
    drawFlameTongue(center, r * 1.32f, Color(0xFF1E5ED8), tiltFactor = 0.20f)
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.15f),
        r = r * 0.96f,
        color = Color(0xFF3FA9F5),
        tiltFactor = -0.18f,
    )
    drawFlameTongue(
        center = Offset(center.x, center.y + r * 0.34f),
        r = r * 0.58f,
        color = Color(0xFFA8E8FF),
        tiltFactor = 0.12f,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0x00A8E8FF)),
            center = Offset(center.x, center.y + r * 0.45f),
            radius = r * 0.4f,
        ),
        radius = r * 0.4f,
        center = Offset(center.x, center.y + r * 0.45f),
    )
    val sparks = listOf(
        Offset(-0.7f, -0.9f) to 0.06f,
        Offset(0.72f, -0.75f) to 0.05f,
        Offset(0.95f, -0.2f) to 0.04f,
    )
    sparks.forEach { (rel, sizeFactor) ->
        drawCircle(
            color = Color(0xFFA8E8FF),
            radius = r * sizeFactor,
            center = Offset(center.x + rel.x * r, center.y + rel.y * r),
        )
    }
}

internal data class CometPalette(
    val core: Color,
    val tailLight: Color,
    val tailDark: Color,
)

internal val cometGreen = CometPalette(
    core = Color(0xFFEFFFF2),
    tailLight = Color(0xFF7BE89A),
    tailDark = Color(0xFF1F9E4C),
)

internal val cometOrange = CometPalette(
    core = Color(0xFFFFF8E8),
    tailLight = Color(0xFFFFC93C),
    tailDark = Color(0xFFE2481B),
)

internal val cometPurple = CometPalette(
    core = Color(0xFFF8F0FF),
    tailLight = Color(0xFFC89AFF),
    tailDark = Color(0xFF6A1FB8),
)

internal fun DrawScope.drawComet(center: Offset, r: Float, palette: CometPalette) {
    val head = Offset(center.x - r * 0.42f, center.y + r * 0.42f)
    val tailTip = Offset(center.x + r * 1.0f, center.y - r * 1.0f)

    drawPath(
        path = Path().apply {
            moveTo(head.x - r * 0.05f, head.y - r * 0.4f)
            lineTo(tailTip.x, tailTip.y)
            lineTo(head.x + r * 0.4f, head.y + r * 0.05f)
            close()
        },
        brush = Brush.linearGradient(
            colors = listOf(palette.tailLight, palette.tailDark.copy(alpha = 0.15f)),
            start = head,
            end = tailTip,
        ),
    )
    drawPath(
        path = Path().apply {
            moveTo(head.x + r * 0.05f, head.y - r * 0.28f)
            lineTo(center.x + r * 0.75f, center.y - r * 0.68f)
            lineTo(head.x + r * 0.28f, head.y - r * 0.02f)
            close()
        },
        color = palette.tailLight.copy(alpha = 0.55f),
    )

    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(palette.tailLight.copy(alpha = 0.6f), Color.Transparent),
            center = head,
            radius = r * 0.65f,
        ),
        radius = r * 0.65f,
        center = head,
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(palette.core, palette.tailLight),
            center = Offset(head.x - r * 0.10f, head.y - r * 0.10f),
            radius = r * 0.52f,
        ),
        radius = r * 0.44f,
        center = head,
    )
    drawCircle(
        color = palette.tailDark,
        radius = r * 0.44f,
        center = head,
        style = Stroke(width = r * 0.05f),
    )

    drawFourPointSparkle(
        center = Offset(center.x + r * 0.55f, center.y + r * 0.35f),
        size = r * 0.12f,
        color = palette.tailLight,
    )
    drawFourPointSparkle(
        center = Offset(center.x - r * 0.15f, center.y - r * 0.72f),
        size = r * 0.09f,
        color = palette.core,
    )
}

private object BoltPalette {
    val light = Color(0xFFFFF6B0)
    val mid = Color(0xFFFFD93C)
    val dark = Color(0xFFE8940A)
    val outline = Color(0xFF9C5F00)
}

internal fun DrawScope.drawBoltSingle(center: Offset, r: Float) {
    drawBoltShape(center, r * 1.05f, glow = false)
}

internal fun DrawScope.drawBoltDouble(center: Offset, r: Float) {
    rotate(degrees = -14f, pivot = center) {
        drawBoltShape(Offset(center.x - r * 0.42f, center.y), r * 0.85f, glow = false)
    }
    rotate(degrees = 10f, pivot = center) {
        drawBoltShape(Offset(center.x + r * 0.40f, center.y + r * 0.08f), r * 1.0f, glow = false)
    }
}

internal fun DrawScope.drawBoltTriple(center: Offset, r: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(BoltPalette.mid.copy(alpha = 0.4f), Color.Transparent),
            center = center,
            radius = r * 1.5f,
        ),
        radius = r * 1.5f,
        center = center,
    )
    rotate(degrees = -20f, pivot = center) {
        drawBoltShape(Offset(center.x - r * 0.58f, center.y + r * 0.05f), r * 0.72f, glow = false)
    }
    rotate(degrees = 20f, pivot = center) {
        drawBoltShape(Offset(center.x + r * 0.58f, center.y + r * 0.05f), r * 0.72f, glow = false)
    }
    drawBoltShape(Offset(center.x, center.y - r * 0.05f), r * 1.1f, glow = true)
}

private fun DrawScope.drawBoltShape(center: Offset, r: Float, glow: Boolean) {
    val path = Path().apply {
        moveTo(center.x + r * 0.22f, center.y - r)
        lineTo(center.x - r * 0.42f, center.y + r * 0.12f)
        lineTo(center.x - r * 0.04f, center.y + r * 0.12f)
        lineTo(center.x - r * 0.26f, center.y + r)
        lineTo(center.x + r * 0.46f, center.y - r * 0.16f)
        lineTo(center.x + r * 0.06f, center.y - r * 0.16f)
        lineTo(center.x + r * 0.42f, center.y - r)
        close()
    }

    translate(left = r * 0.05f, top = r * 0.06f) {
        drawPath(path = path, color = Color(0x40000000))
    }

    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = if (glow) {
                listOf(Color.White, BoltPalette.light, BoltPalette.mid)
            } else {
                listOf(BoltPalette.light, BoltPalette.mid, BoltPalette.dark)
            },
            start = Offset(center.x, center.y - r),
            end = Offset(center.x, center.y + r),
        ),
    )
    drawPath(
        path = path,
        color = BoltPalette.outline,
        style = Stroke(width = r * 0.06f),
    )
}