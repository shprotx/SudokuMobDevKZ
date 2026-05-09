package ru.shprot.sudokumobdevkz.core.base.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

private const val TwoRowFitThresholdDp = 380

@Composable
fun deviceFitsTwoRowInPortrait(): Boolean {
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp
    val height = configuration.screenHeightDp
    val portraitHeight = maxOf(width, height)
    val portraitWidth = minOf(width, height)
    return (portraitHeight - portraitWidth) >= TwoRowFitThresholdDp
}