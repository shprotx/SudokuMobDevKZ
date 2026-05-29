package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

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
    colors: AppColors,
    content: @Composable () -> Unit,
) {
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
