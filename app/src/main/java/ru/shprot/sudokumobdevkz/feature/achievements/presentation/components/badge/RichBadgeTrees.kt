package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.badge

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate

private object TreePalette {
    val groundLight = Color(0xFF9C7A4A)
    val groundDark = Color(0xFF6B4E2A)
    val trunkLight = Color(0xFFA9744C)
    val trunkDark = Color(0xFF6E4426)
    val crownLight = Color(0xFFA8E66E)
    val crownMid = Color(0xFF5FB84A)
    val crownDark = Color(0xFF2F7F2E)
    val blossomPink = Color(0xFFFFB7D5)
    val blossomCore = Color(0xFFFFF0F7)
    val fruitRed = Color(0xFFE8503A)
    val fruitHighlight = Color(0xFFFFB49E)
    val goldLight = Color(0xFFFFE08A)
    val goldDark = Color(0xFFCF9219)
    val mysticLight = Color(0xFF8FE8D8)
    val mysticDark = Color(0xFF1E6E62)
}

private fun DrawScope.drawGroundMound(center: Offset, r: Float, widthFactor: Float = 0.85f) {
    drawOval(
        brush = Brush.verticalGradient(
            colors = listOf(TreePalette.groundLight, TreePalette.groundDark),
            startY = center.y + r * 0.62f,
            endY = center.y + r * 0.95f,
        ),
        topLeft = Offset(center.x - r * widthFactor, center.y + r * 0.62f),
        size = Size(r * widthFactor * 2f, r * 0.33f),
    )
}

private fun DrawScope.drawTrunk(
    center: Offset,
    r: Float,
    topY: Float,
    baseWidth: Float,
    lean: Float = 0f,
) {
    val baseY = center.y + r * 0.78f
    drawPath(
        path = Path().apply {
            moveTo(center.x - baseWidth, baseY)
            cubicTo(
                center.x - baseWidth * 0.55f, center.y + r * 0.2f,
                center.x - baseWidth * 0.4f + lean, topY + r * 0.15f,
                center.x - baseWidth * 0.28f + lean, topY,
            )
            lineTo(center.x + baseWidth * 0.28f + lean, topY)
            cubicTo(
                center.x + baseWidth * 0.4f + lean, topY + r * 0.15f,
                center.x + baseWidth * 0.55f, center.y + r * 0.2f,
                center.x + baseWidth, baseY,
            )
            close()
        },
        brush = Brush.horizontalGradient(
            colors = listOf(TreePalette.trunkLight, TreePalette.trunkDark),
            startX = center.x - baseWidth,
            endX = center.x + baseWidth,
        ),
    )
}

private fun DrawScope.drawCrownBlob(
    center: Offset,
    radius: Float,
    light: Color = TreePalette.crownLight,
    dark: Color = TreePalette.crownDark,
) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(light, dark),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.35f),
            radius = radius * 1.5f,
        ),
        radius = radius,
        center = center,
    )
}

private fun DrawScope.drawCrownHighlights(center: Offset, r: Float, count: Int) {
    val spots = listOf(
        Offset(-0.35f, -0.5f) to 0.08f,
        Offset(0.3f, -0.62f) to 0.06f,
        Offset(0.5f, -0.3f) to 0.055f,
        Offset(-0.55f, -0.15f) to 0.05f,
        Offset(0.05f, -0.28f) to 0.045f,
    )
    spots.take(count).forEach { (rel, sizeFactor) ->
        drawCircle(
            color = Color.White.copy(alpha = 0.35f),
            radius = r * sizeFactor,
            center = Offset(center.x + rel.x * r, center.y + rel.y * r),
        )
    }
}

private fun DrawScope.drawStemLeaf(base: Offset, len: Float, angleDeg: Float) {
    rotate(degrees = angleDeg, pivot = base) {
        drawPath(
            path = Path().apply {
                moveTo(base.x, base.y)
                cubicTo(
                    base.x + len * 0.5f, base.y - len * 0.3f,
                    base.x + len * 0.5f, base.y - len * 0.78f,
                    base.x, base.y - len,
                )
                cubicTo(
                    base.x - len * 0.5f, base.y - len * 0.78f,
                    base.x - len * 0.5f, base.y - len * 0.3f,
                    base.x, base.y,
                )
                close()
            },
            brush = Brush.verticalGradient(
                colors = listOf(TreePalette.crownLight, TreePalette.crownMid),
                startY = base.y - len,
                endY = base.y,
            ),
        )
    }
}

internal fun DrawScope.drawTreeSproutBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.6f)
    drawLine(
        color = TreePalette.crownDark,
        start = Offset(center.x, center.y + r * 0.7f),
        end = Offset(center.x, center.y - r * 0.15f),
        strokeWidth = r * 0.09f,
        cap = StrokeCap.Round,
    )
    drawStemLeaf(Offset(center.x - r * 0.02f, center.y + r * 0.1f), r * 0.55f, -48f)
    drawStemLeaf(Offset(center.x + r * 0.02f, center.y + r * 0.1f), r * 0.55f, 48f)
    drawStemLeaf(Offset(center.x, center.y - r * 0.12f), r * 0.5f, 0f)
}

internal fun DrawScope.drawTreeSaplingBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.68f)
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.04f, center.y + r * 0.72f)
            cubicTo(
                center.x + r * 0.06f, center.y + r * 0.2f,
                center.x - r * 0.06f, center.y - r * 0.25f,
                center.x + r * 0.03f, center.y - r * 0.62f,
            )
        },
        color = TreePalette.trunkDark,
        style = Stroke(width = r * 0.1f, cap = StrokeCap.Round),
    )
    drawStemLeaf(Offset(center.x - r * 0.02f, center.y + r * 0.34f), r * 0.62f, -58f)
    drawStemLeaf(Offset(center.x + r * 0.02f, center.y + r * 0.08f), r * 0.66f, 56f)
    drawStemLeaf(Offset(center.x - r * 0.03f, center.y - r * 0.18f), r * 0.64f, -52f)
    drawStemLeaf(Offset(center.x + r * 0.04f, center.y - r * 0.4f), r * 0.6f, 48f)
    drawStemLeaf(Offset(center.x + r * 0.03f, center.y - r * 0.56f), r * 0.62f, -4f)
}

internal fun DrawScope.drawTreeBushBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.8f)
    drawBushBlob(Offset(center.x - r * 0.42f, center.y + r * 0.3f), r * 0.4f)
    drawBushBlob(Offset(center.x + r * 0.42f, center.y + r * 0.3f), r * 0.4f)
    drawBushBlob(Offset(center.x, center.y - r * 0.12f), r * 0.5f)
    drawCrownHighlights(center, r, count = 4)
}

private fun DrawScope.drawBushBlob(center: Offset, radius: Float) {
    drawCrownBlob(center, radius)
    drawCircle(
        color = Color(0xFF1E5C1E).copy(alpha = 0.65f),
        radius = radius,
        center = center,
        style = Stroke(width = radius * 0.07f),
    )
}

internal fun DrawScope.drawTreeYoungBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.7f)
    drawTrunk(center, r, topY = center.y - r * 0.05f, baseWidth = r * 0.16f)
    drawCrownBlob(Offset(center.x, center.y - r * 0.38f), r * 0.5f)
    drawCrownHighlights(Offset(center.x, center.y - r * 0.38f), r * 0.75f, count = 3)
}

internal fun DrawScope.drawTreeClassicBadge(center: Offset, r: Float) {
    drawGroundMound(center, r)
    drawTrunk(center, r, topY = center.y - r * 0.1f, baseWidth = r * 0.2f)
    drawCrownBlob(Offset(center.x - r * 0.4f, center.y - r * 0.22f), r * 0.4f)
    drawCrownBlob(Offset(center.x + r * 0.4f, center.y - r * 0.22f), r * 0.4f)
    drawCrownBlob(Offset(center.x, center.y - r * 0.5f), r * 0.5f)
    drawCrownHighlights(center, r, count = 5)
}

internal fun DrawScope.drawTreeBlossomBadge(center: Offset, r: Float) {
    drawTreeClassicBadge(center, r)
    val blossoms = listOf(
        Offset(-0.42f, -0.38f) to 0.1f,
        Offset(0.08f, -0.66f) to 0.09f,
        Offset(0.46f, -0.3f) to 0.1f,
        Offset(-0.12f, -0.3f) to 0.08f,
        Offset(0.3f, -0.52f) to 0.075f,
        Offset(-0.5f, -0.1f) to 0.07f,
    )
    blossoms.forEach { (rel, sizeFactor) ->
        val pos = Offset(center.x + rel.x * r, center.y + rel.y * r)
        drawCircle(color = TreePalette.blossomPink, radius = r * sizeFactor, center = pos)
        drawCircle(color = TreePalette.blossomCore, radius = r * sizeFactor * 0.45f, center = pos)
    }
}

internal fun DrawScope.drawTreeFruitBadge(center: Offset, r: Float) {
    drawTreeClassicBadge(center, r)
    val fruits = listOf(
        Offset(-0.4f, -0.32f) to 0.11f,
        Offset(0.12f, -0.6f) to 0.1f,
        Offset(0.44f, -0.26f) to 0.11f,
        Offset(-0.05f, -0.26f) to 0.09f,
        Offset(0.28f, -0.44f) to 0.085f,
    )
    fruits.forEach { (rel, sizeFactor) ->
        val pos = Offset(center.x + rel.x * r, center.y + rel.y * r)
        drawCircle(color = TreePalette.fruitRed, radius = r * sizeFactor, center = pos)
        drawCircle(
            color = TreePalette.fruitHighlight,
            radius = r * sizeFactor * 0.35f,
            center = Offset(pos.x - r * sizeFactor * 0.3f, pos.y - r * sizeFactor * 0.3f),
        )
    }
}

internal fun DrawScope.drawTreeMightyBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.95f)
    drawTrunk(center, r, topY = center.y - r * 0.05f, baseWidth = r * 0.3f)
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.3f, center.y + r * 0.78f)
            quadraticTo(
                center.x - r * 0.52f, center.y + r * 0.72f,
                center.x - r * 0.62f, center.y + r * 0.78f,
            )
            lineTo(center.x - r * 0.3f, center.y + r * 0.78f)
            close()
        },
        color = TreePalette.trunkDark,
    )
    val darkLight = Color(0xFF9FE374)
    val darkDark = Color(0xFF2E7E3A)
    drawCrownBlob(Offset(center.x - r * 0.52f, center.y - r * 0.12f), r * 0.38f, darkLight, darkDark)
    drawCrownBlob(Offset(center.x + r * 0.52f, center.y - r * 0.12f), r * 0.38f, darkLight, darkDark)
    drawCrownBlob(Offset(center.x - r * 0.28f, center.y - r * 0.44f), r * 0.42f, darkLight, darkDark)
    drawCrownBlob(Offset(center.x + r * 0.28f, center.y - r * 0.44f), r * 0.42f, darkLight, darkDark)
    drawCrownBlob(Offset(center.x, center.y - r * 0.62f), r * 0.44f, darkLight, darkDark)
    drawCrownHighlights(center, r * 1.1f, count = 5)
}

internal fun DrawScope.drawTreeGoldenBadge(center: Offset, r: Float) {
    drawGroundMound(center, r, widthFactor = 0.9f)
    drawTrunk(center, r, topY = center.y - r * 0.08f, baseWidth = r * 0.24f)
    drawCrownBlob(
        Offset(center.x - r * 0.42f, center.y - r * 0.2f),
        r * 0.4f,
        TreePalette.goldLight,
        TreePalette.goldDark,
    )
    drawCrownBlob(
        Offset(center.x + r * 0.42f, center.y - r * 0.2f),
        r * 0.4f,
        TreePalette.goldLight,
        TreePalette.goldDark,
    )
    drawCrownBlob(
        Offset(center.x, center.y - r * 0.52f),
        r * 0.5f,
        TreePalette.goldLight,
        TreePalette.goldDark,
    )
    drawCrownHighlights(center, r, count = 4)
    drawFourPointSparkle(
        center = Offset(center.x - r * 0.3f, center.y - r * 0.55f),
        size = r * 0.14f,
        color = Color.White,
    )
    drawFourPointSparkle(
        center = Offset(center.x + r * 0.42f, center.y - r * 0.05f),
        size = r * 0.1f,
        color = Color.White.copy(alpha = 0.85f),
    )
    listOf(
        Offset(-0.6f, 0.3f) to 0.05f,
        Offset(0.55f, 0.45f) to 0.045f,
        Offset(0.1f, 0.5f) to 0.04f,
    ).forEach { (rel, sizeFactor) ->
        drawCircle(
            color = TreePalette.goldLight,
            radius = r * sizeFactor,
            center = Offset(center.x + rel.x * r, center.y + rel.y * r),
        )
    }
}

internal fun DrawScope.drawTreeWorldBadge(center: Offset, r: Float) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(TreePalette.mysticLight.copy(alpha = 0.55f), Color.Transparent),
            center = Offset(center.x, center.y - r * 0.25f),
            radius = r * 1.1f,
        ),
        radius = r * 1.05f,
        center = Offset(center.x, center.y - r * 0.15f),
    )
    drawGroundMound(center, r, widthFactor = 0.95f)
    drawTrunk(center, r, topY = center.y - r * 0.02f, baseWidth = r * 0.28f)
    drawPath(
        path = Path().apply {
            moveTo(center.x - r * 0.1f, center.y + r * 0.3f)
            cubicTo(
                center.x - r * 0.35f, center.y + r * 0.1f,
                center.x - r * 0.4f, center.y - r * 0.1f,
                center.x - r * 0.34f, center.y - r * 0.2f,
            )
        },
        color = TreePalette.trunkLight.copy(alpha = 0.6f),
        style = Stroke(width = r * 0.05f, cap = StrokeCap.Round),
    )
    drawCrownBlob(
        Offset(center.x - r * 0.5f, center.y - r * 0.16f),
        r * 0.4f,
        TreePalette.mysticLight,
        TreePalette.mysticDark,
    )
    drawCrownBlob(
        Offset(center.x + r * 0.5f, center.y - r * 0.16f),
        r * 0.4f,
        TreePalette.mysticLight,
        TreePalette.mysticDark,
    )
    drawCrownBlob(
        Offset(center.x - r * 0.26f, center.y - r * 0.48f),
        r * 0.42f,
        TreePalette.mysticLight,
        TreePalette.mysticDark,
    )
    drawCrownBlob(
        Offset(center.x + r * 0.26f, center.y - r * 0.48f),
        r * 0.42f,
        TreePalette.mysticLight,
        TreePalette.mysticDark,
    )
    drawCrownBlob(
        Offset(center.x, center.y - r * 0.66f),
        r * 0.42f,
        TreePalette.mysticLight,
        TreePalette.mysticDark,
    )
    listOf(
        Offset(-0.42f, -0.5f),
        Offset(0.05f, -0.72f),
        Offset(0.44f, -0.42f),
        Offset(-0.1f, -0.36f),
        Offset(0.28f, -0.2f),
    ).forEachIndexed { index, rel ->
        drawFourPointSparkle(
            center = Offset(center.x + rel.x * r, center.y + rel.y * r),
            size = r * if (index % 2 == 0) 0.1f else 0.075f,
            color = Color.White.copy(alpha = if (index % 2 == 0) 0.95f else 0.7f),
        )
    }
    drawCircle(
        brush = Brush.linearGradient(
            colors = listOf(TreePalette.goldLight, Color.Transparent),
            start = Offset(center.x - r * 0.6f, center.y - r * 0.9f),
            end = Offset(center.x + r * 0.4f, center.y + r * 0.1f),
        ),
        radius = r * 0.85f,
        center = Offset(center.x, center.y - r * 0.28f),
        style = Stroke(width = r * 0.035f),
    )
}
