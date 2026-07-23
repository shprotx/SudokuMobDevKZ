package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components

import android.graphics.Color as AndroidColor

internal data class HsvColor(
    val hue: Float,
    val saturation: Float,
    val value: Float,
)

internal fun argbToHsv(argb: Long): HsvColor {
    val hsv = FloatArray(3)
    AndroidColor.colorToHSV(argb.toInt(), hsv)
    return HsvColor(hue = hsv[0], saturation = hsv[1], value = hsv[2])
}

internal fun HsvColor.toArgb(alpha: Long): Long {
    val rgb = AndroidColor.HSVToColor(floatArrayOf(hue, saturation, value)).toLong() and 0xFFFFFF
    return (alpha shl 24) or rgb
}