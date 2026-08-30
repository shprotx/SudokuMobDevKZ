package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

internal fun argbRed(argb: Long): Int = ((argb shr 16) and 0xFF).toInt()

internal fun argbGreen(argb: Long): Int = ((argb shr 8) and 0xFF).toInt()

internal fun argbBlue(argb: Long): Int = (argb and 0xFF).toInt()

internal fun argbAlpha(argb: Long): Long = (argb shr 24) and 0xFF

internal fun composeArgb(alpha: Long, red: Int, green: Int, blue: Int): Long =
    (alpha shl 24) or
        ((red.toLong() and 0xFF) shl 16) or
        ((green.toLong() and 0xFF) shl 8) or
        (blue.toLong() and 0xFF)

internal val quickColorSwatches: List<Long> = listOf(
    Color(0xFF1A1A1A),
    Color(0xFF636366),
    Color(0xFFC7C7CC),
    Color(0xFFFFFFFF),
    Color(0xFFFF3B30),
    Color(0xFFFF9500),
    Color(0xFFFFCC00),
    Color(0xFF34C759),
    Color(0xFF00C7BE),
    Color(0xFF32ADE6),
    Color(0xFF007AFF),
    Color(0xFF5856D6),
    Color(0xFFAF52DE),
    Color(0xFFFF2D55),
).map { it.toArgb().toLong() and 0xFFFFFFFFL }
