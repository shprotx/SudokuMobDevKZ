package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Padding(
    val extraSmall: Dp = 2.dp,
    val small: Dp = 4.dp,
    val medium: Dp = 8.dp,
    val default: Dp = 12.dp,
    val large: Dp = 16.dp,
    val extraLarge: Dp = 20.dp,
    val xxl: Dp = 24.dp,
    val xxxl: Dp = 32.dp,
    val section: Dp = 40.dp,
    val screen: Dp = 16.dp,
)

internal val LocalAppPaddings = staticCompositionLocalOf { Padding() }
