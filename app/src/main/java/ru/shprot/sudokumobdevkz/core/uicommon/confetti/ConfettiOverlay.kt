package ru.shprot.sudokumobdevkz.core.uicommon.confetti

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.random.Random

private val ConfettiPalette = listOf(
    Color(0xFFFFE066),
    Color(0xFFFF9500),
    Color(0xFFFF3B30),
    Color(0xFF34C759),
    Color(0xFF007AFF),
    Color(0xFFAF52DE),
    Color(0xFFFF2D92),
    Color(0xFF00C6FF),
)

@Composable
fun ConfettiOverlay(
    modifier: Modifier,
    particleCount: Int = 90,
    durationMillis: Int = 2400,
) {
    val particles = remember { generateConfetti(particleCount) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(targetValue = 1f, animationSpec = tween(durationMillis))
    }

    Canvas(modifier = modifier) {
        val t = progress.value
        val canvasW = size.width
        val canvasH = size.height

        particles.forEach { p ->
            val gravityTerm = 0.55f * t * t / p.weight
            val xFraction = p.startX + p.vx * t
            val yFraction = p.startY + p.vy * t + gravityTerm
            val x = xFraction * canvasW
            val y = yFraction * canvasH
            val angle = p.rotationStart + p.rotationSpeed * t
            val fadeStart = 0.8f
            val alpha = if (t < fadeStart) 1f else 1f - (t - fadeStart) / (1f - fadeStart)

            rotate(degrees = angle, pivot = Offset(x, y)) {
                drawRect(
                    color = p.color.copy(alpha = alpha.coerceIn(0f, 1f)),
                    topLeft = Offset(x - p.width / 2f, y - p.height / 2f),
                    size = Size(p.width, p.height),
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val startX: Float,
    val startY: Float,
    val vx: Float,
    val vy: Float,
    val weight: Float,
    val color: Color,
    val width: Float,
    val height: Float,
    val rotationStart: Float,
    val rotationSpeed: Float,
)

private fun generateConfetti(count: Int): List<ConfettiParticle> = List(count) {
    ConfettiParticle(
        startX = Random.nextFloat() * 0.7f + 0.15f,
        startY = -Random.nextFloat() * 0.1f,
        vx = Random.nextFloat() * 1.0f - 0.5f,
        vy = Random.nextFloat() * 0.2f - 0.05f,
        weight = Random.nextFloat() * 0.6f + 0.7f,
        color = ConfettiPalette.random(),
        width = Random.nextFloat() * 6f + 8f,
        height = Random.nextFloat() * 10f + 6f,
        rotationStart = Random.nextFloat() * 360f,
        rotationSpeed = Random.nextFloat() * 720f - 360f,
    )
}
