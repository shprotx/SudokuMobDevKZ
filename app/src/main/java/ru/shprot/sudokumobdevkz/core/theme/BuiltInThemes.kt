package ru.shprot.sudokumobdevkz.core.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import ru.shprot.sudokumobdevkz.core.base.domain.model.CustomTheme
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors

enum class BuiltInTheme(
    val id: String,
    val themeName: String,
) {
    LIGHT("BUILT_IN_LIGHT", "Light"),
    DARK("BUILT_IN_DARK", "Dark"),
    SOLARIZED("BUILT_IN_SOLARIZED", "Solarized"),
    FOREST("BUILT_IN_FOREST", "Forest");

    val colors: ThemeColors
        get() = when (this) {
            LIGHT -> AppColors.LightColors.toThemeColors()
            DARK -> AppColors.DarkColors.toThemeColors()
            SOLARIZED -> solarizedColors()
            FOREST -> forestColors()
        }

    fun toCustomTheme(): CustomTheme = CustomTheme(
        id = id,
        name = themeName,
        isBuiltIn = true,
        colors = colors,
        createdAt = 0L,
    )

    companion object {
        fun customThemes(): List<CustomTheme> = entries.map { it.toCustomTheme() }
    }
}

private fun solarizedColors(): ThemeColors {
    val bg = Color(0xFFFDF6E3)
    val surface = Color(0xFFEEE8D5)
    val primary = Color(0xFF268BD2)
    val text = Color(0xFF657B83)
    val textSecondary = Color(0xFF93A1A1)
    val accent = Color(0xFF859900)
    val error = Color(0xFFDC322F)
    val warning = Color(0xFFCB4B16)
    val divider = Color(0xFFD3C9B2)
    val iconTint = Color(0xFF839496)
    return ThemeColors(
        primary = primary.toArgbLong(),
        primaryDark = Color(0xFF1E6EA6).toArgbLong(),
        primaryLight = Color(0xFFD6EAF7).toArgbLong(),
        secondary = surface.toArgbLong(),
        background = bg.toArgbLong(),
        backgroundCard = Color(0xFFF5EFDF).toArgbLong(),
        backgroundCardAccent = Color(0xFFD6EAF7).toArgbLong(),
        surface = surface.toArgbLong(),
        text = text.toArgbLong(),
        textSecondary = textSecondary.toArgbLong(),
        textOnPrimary = Color(0xFFFFFFFF).toArgbLong(),
        textAccent = accent.toArgbLong(),
        error = error.toArgbLong(),
        errorLight = Color(0xFFF8D7DA).toArgbLong(),
        success = accent.toArgbLong(),
        warning = warning.toArgbLong(),
        gridLine = Color(0xFFC0B49A).toArgbLong(),
        gridLineBold = Color(0xFF839496).toArgbLong(),
        cellSelected = Color(0xFFBDD6E9).toArgbLong(),
        cellHighlight = Color(0xFFD6E8F4).toArgbLong(),
        cellSameNumber = Color(0xFFC9DCE8).toArgbLong(),
        cellError = error.copy(alpha = 0.15f).toArgbLong(),
        cellFixed = Color(0xFF586E75).toArgbLong(),
        cellEditable = Color(0xFF268BD2).toArgbLong(),
        draftText = textSecondary.toArgbLong(),
        draftHighlight = text.toArgbLong(),
        divider = divider.toArgbLong(),
        iconTint = iconTint.toArgbLong(),
        bottomNavSelected = primary.toArgbLong(),
        bottomNavUnselected = Color(0xFFADB8BB).toArgbLong(),
        chipSelected = primary.toArgbLong(),
        chipUnselected = surface.toArgbLong(),
        chipTextSelected = Color(0xFFFFFFFF).toArgbLong(),
        chipTextUnselected = text.toArgbLong(),
        progressTrack = divider.toArgbLong(),
        progressIndicator = primary.toArgbLong(),
        barChart = primary.toArgbLong(),
        barChartLabel = textSecondary.toArgbLong(),
    )
}

private fun forestColors(): ThemeColors {
    val bg = Color(0xFF1A2416)
    val surface = Color(0xFF243020)
    val primary = Color(0xFF4CAF50)
    val text = Color(0xFFB5CC90)
    val textSecondary = Color(0xFF7A9965)
    val accent = Color(0xFFAED581)
    val error = Color(0xFFEF5350)
    val warning = Color(0xFFFF8F00)
    val divider = Color(0xFF2E3D28)
    val iconTint = Color(0xFF7A9965)
    return ThemeColors(
        primary = primary.toArgbLong(),
        primaryDark = Color(0xFF388E3C).toArgbLong(),
        primaryLight = Color(0xFF1E3B21).toArgbLong(),
        secondary = surface.toArgbLong(),
        background = bg.toArgbLong(),
        backgroundCard = surface.toArgbLong(),
        backgroundCardAccent = Color(0xFF1E3B21).toArgbLong(),
        surface = surface.toArgbLong(),
        text = text.toArgbLong(),
        textSecondary = textSecondary.toArgbLong(),
        textOnPrimary = Color(0xFF0D1A0A).toArgbLong(),
        textAccent = accent.toArgbLong(),
        error = error.toArgbLong(),
        errorLight = Color(0xFF3B1A1A).toArgbLong(),
        success = primary.toArgbLong(),
        warning = warning.toArgbLong(),
        gridLine = Color(0xFF3C5234).toArgbLong(),
        gridLineBold = Color(0xFF8AAE72).toArgbLong(),
        cellSelected = Color(0xFF2E5C2E).copy(alpha = 0.8f).toArgbLong(),
        cellHighlight = Color(0xFF1E4020).copy(alpha = 0.6f).toArgbLong(),
        cellSameNumber = Color(0xFF4A6B3A).copy(alpha = 0.4f).toArgbLong(),
        cellError = error.copy(alpha = 0.25f).toArgbLong(),
        cellFixed = Color(0xFFAED581).toArgbLong(),
        cellEditable = Color(0xFF81C784).toArgbLong(),
        draftText = textSecondary.toArgbLong(),
        draftHighlight = text.toArgbLong(),
        divider = divider.toArgbLong(),
        iconTint = iconTint.toArgbLong(),
        bottomNavSelected = primary.toArgbLong(),
        bottomNavUnselected = Color(0xFF4A6040).toArgbLong(),
        chipSelected = primary.toArgbLong(),
        chipUnselected = Color(0xFF2C3E26).toArgbLong(),
        chipTextSelected = Color(0xFF0D1A0A).toArgbLong(),
        chipTextUnselected = textSecondary.toArgbLong(),
        progressTrack = divider.toArgbLong(),
        progressIndicator = primary.toArgbLong(),
        barChart = primary.toArgbLong(),
        barChartLabel = textSecondary.toArgbLong(),
    )
}

fun AppColors.toThemeColors(): ThemeColors = ThemeColors(
    primary = primary.toArgbLong(),
    primaryDark = primaryDark.toArgbLong(),
    primaryLight = primaryLight.toArgbLong(),
    secondary = secondary.toArgbLong(),
    background = background.toArgbLong(),
    backgroundCard = backgroundCard.toArgbLong(),
    backgroundCardAccent = backgroundCardAccent.toArgbLong(),
    surface = surface.toArgbLong(),
    text = text.toArgbLong(),
    textSecondary = textSecondary.toArgbLong(),
    textOnPrimary = textOnPrimary.toArgbLong(),
    textAccent = textAccent.toArgbLong(),
    error = error.toArgbLong(),
    errorLight = errorLight.toArgbLong(),
    success = success.toArgbLong(),
    warning = warning.toArgbLong(),
    gridLine = gridLine.toArgbLong(),
    gridLineBold = gridLineBold.toArgbLong(),
    cellSelected = cellSelected.toArgbLong(),
    cellHighlight = cellHighlight.toArgbLong(),
    cellSameNumber = cellSameNumber.toArgbLong(),
    cellError = cellError.toArgbLong(),
    cellFixed = cellFixed.toArgbLong(),
    cellEditable = cellEditable.toArgbLong(),
    draftText = draftText.toArgbLong(),
    draftHighlight = draftHighlight.toArgbLong(),
    divider = divider.toArgbLong(),
    iconTint = iconTint.toArgbLong(),
    bottomNavSelected = bottomNavSelected.toArgbLong(),
    bottomNavUnselected = bottomNavUnselected.toArgbLong(),
    chipSelected = chipSelected.toArgbLong(),
    chipUnselected = chipUnselected.toArgbLong(),
    chipTextSelected = chipTextSelected.toArgbLong(),
    chipTextUnselected = chipTextUnselected.toArgbLong(),
    progressTrack = progressTrack.toArgbLong(),
    progressIndicator = progressIndicator.toArgbLong(),
    barChart = barChart.toArgbLong(),
    barChartLabel = barChartLabel.toArgbLong(),
)

private fun Color.toArgbLong(): Long = toArgb().toLong() and 0xFFFFFFFFL
