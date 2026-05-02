package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Size(
    val iconSmall: Dp = 16.dp,
    val iconMedium: Dp = 24.dp,
    val iconLarge: Dp = 32.dp,
    val iconXL: Dp = 48.dp,
    val buttonHeight: Dp = 56.dp,
    val buttonHeightSmall: Dp = 40.dp,
    val cardMinHeight: Dp = 80.dp,
    val chipHeight: Dp = 36.dp,
    val bottomNavHeight: Dp = 64.dp,
    val toolbarHeight: Dp = 56.dp,
    val numberPanelButton: Dp = 34.dp,
    val gridCell: Dp = 40.dp,
    val difficultyDot: Dp = 6.dp,
    val cornerRadiusSmall: Dp = 8.dp,
    val cornerRadiusMedium: Dp = 12.dp,
    val cornerRadiusLarge: Dp = 16.dp,
    val cornerRadiusXL: Dp = 20.dp,
    val cornerRadiusFull: Dp = 100.dp,
    val dividerThickness: Dp = 1.dp,
    val elevationSmall: Dp = 2.dp,
    val elevationMedium: Dp = 4.dp,
    val elevationLarge: Dp = 8.dp,
)

internal val LocalAppSizes = staticCompositionLocalOf { Size() }
