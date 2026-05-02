package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

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

class TextStyles {

    private val defaultFontFamily = FontFamily.Default

    val h1 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
    )

    val h2 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    val h3 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
    )

    val h4 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    )

    val body1 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )

    val body2 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )

    val body3 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )

    val body4 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
    )

    val body5 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )

    val caption1 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 11.sp,
    )

    val caption2 = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
    )

    val button = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    )

    val gridNumber = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
    )

    val gridDraft = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
    )

    val timer = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    )

    val statValue = TextStyle(
        fontFamily = defaultFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
    )
}

internal val LocalAppTypography = staticCompositionLocalOf {
    appTypography(TextStyles())
}
