package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class AppTypography(
    val materialTypography: Typography,
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val body4: TextStyle,
    val body5: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
    val button: TextStyle,
    val gridNumber: TextStyle,
    val gridDraft: TextStyle,
    val timer: TextStyle,
    val statValue: TextStyle,
)

internal fun appTypography(styles: TextStyles): AppTypography =
    with(styles) {
        AppTypography(
            materialTypography = Typography(),
            h1 = h1,
            h2 = h2,
            h3 = h3,
            h4 = h4,
            body1 = body1,
            body2 = body2,
            body3 = body3,
            body4 = body4,
            body5 = body5,
            caption1 = caption1,
            caption2 = caption2,
            button = button,
            gridNumber = gridNumber,
            gridDraft = gridDraft,
            timer = timer,
            statValue = statValue,
        )
    }

internal val LocalAppTypography = staticCompositionLocalOf {
    appTypography(TextStyles())
}
