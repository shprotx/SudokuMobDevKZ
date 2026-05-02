package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

object AppTheme {

    val colors: AppColors
        @Composable
        @ReadOnlyComposable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTypography.current

    val paddings: Padding
        @Composable
        @ReadOnlyComposable
        get() = LocalAppPaddings.current

    val sizes: Size
        @Composable
        @ReadOnlyComposable
        get() = LocalAppSizes.current
}

@Composable
fun SudokuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors: AppColors = remember(darkTheme) {
        when (darkTheme) {
            true -> AppColors.DarkColors
            false -> AppColors.LightColors
        }
    }
    val typography: AppTypography = appTypography(TextStyles())

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppPaddings provides AppTheme.paddings,
        LocalAppSizes provides AppTheme.sizes,
    ) {
        content()
    }
}
