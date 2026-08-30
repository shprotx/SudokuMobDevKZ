package ru.shprot.sudokumobdevkz.core.theme

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.ThemeColors

data class ThemePalette(
    val id: String,
    @StringRes val labelRes: Int,
    val isDark: Boolean,
    val colors: ThemeColors,
)

object ThemePalettes {

    val all: List<ThemePalette> = listOf(
        palette("PRESET_SOLARIZED_LIGHT", R.string.theme_palette_solarized_light, bg = 0xFFFDF6E3, surface = 0xFFEEE8D5, primary = 0xFF1E6FA8, accent = 0xFF6B7D00, text = 0xFF4A6068, textSecondary = 0xFF6E7E7E, error = 0xFFDC322F, cellFixed = 0xFF586E75, cellEditable = 0xFF1E6FA8, divider = 0xFFD3C9B2),
        palette("PRESET_SOLARIZED_DARK", R.string.theme_palette_solarized_dark, bg = 0xFF002B36, surface = 0xFF073642, primary = 0xFF268BD2, accent = 0xFFB58900, text = 0xFF93A1A1, textSecondary = 0xFF657B83, error = 0xFFDC322F, cellFixed = 0xFFEEE8D5, cellEditable = 0xFF2AA198, divider = 0xFF0E4D5E),
        palette("PRESET_SAKURA", R.string.theme_palette_sakura, bg = 0xFFFFF5F7, surface = 0xFFFFFFFF, primary = 0xFFD14E7E, accent = 0xFFC2456F, text = 0xFF4A2733, textSecondary = 0xFF9E7A86, error = 0xFFE53935, cellFixed = 0xFF6D3B4E, cellEditable = 0xFFC2456F, divider = 0xFFF6E2E8),
        palette("PRESET_FOREST", R.string.theme_palette_forest, bg = 0xFF1A2416, surface = 0xFF243020, primary = 0xFF4CAF50, accent = 0xFFAED581, text = 0xFFB5CC90, textSecondary = 0xFF7A9965, error = 0xFFEF5350, cellFixed = 0xFFAED581, cellEditable = 0xFF81C784, divider = 0xFF2E3D28),
        palette("PRESET_DRACULA", R.string.theme_palette_dracula, bg = 0xFF282A36, surface = 0xFF343746, primary = 0xFFBD93F9, accent = 0xFFFF79C6, text = 0xFFF8F8F2, textSecondary = 0xFF9CA0B0, error = 0xFFFF5555, cellFixed = 0xFFF8F8F2, cellEditable = 0xFF8BE9FD, divider = 0xFF44475A),
        palette("PRESET_NORD", R.string.theme_palette_nord, bg = 0xFF2E3440, surface = 0xFF3B4252, primary = 0xFF88C0D0, accent = 0xFFA3BE8C, text = 0xFFECEFF4, textSecondary = 0xFF9BA3B4, error = 0xFFBF616A, cellFixed = 0xFFECEFF4, cellEditable = 0xFF8FBCBB, divider = 0xFF434C5E),
        palette("PRESET_OCEAN", R.string.theme_palette_ocean, bg = 0xFFEBF5FB, surface = 0xFFFFFFFF, primary = 0xFF0277BD, accent = 0xFF00ACC1, text = 0xFF0D3B53, textSecondary = 0xFF5A8199, error = 0xFFE53935, cellFixed = 0xFF0D3B53, cellEditable = 0xFF0288D1, divider = 0xFFCDE5EF),
        palette("PRESET_SUNSET", R.string.theme_palette_sunset, bg = 0xFF2B1B2D, surface = 0xFF3A2438, primary = 0xFFFF8A5C, accent = 0xFFFFB74D, text = 0xFFFFE9DD, textSecondary = 0xFFC79BA8, error = 0xFFFF5252, cellFixed = 0xFFFFD9B3, cellEditable = 0xFFFFAB76, divider = 0xFF4E3450),
        palette("PRESET_LAVENDER", R.string.theme_palette_lavender, bg = 0xFFF5F3FB, surface = 0xFFFFFFFF, primary = 0xFF7E57C2, accent = 0xFF9575CD, text = 0xFF2E2640, textSecondary = 0xFF847B9E, error = 0xFFE53935, cellFixed = 0xFF4A3B73, cellEditable = 0xFF7E57C2, divider = 0xFFE3DCF0),
        palette("PRESET_MINT", R.string.theme_palette_mint, bg = 0xFFEFFAF5, surface = 0xFFFFFFFF, primary = 0xFF0E8A66, accent = 0xFF0E8A8E, text = 0xFF13413A, textSecondary = 0xFF5E8C82, error = 0xFFE53935, cellFixed = 0xFF13413A, cellEditable = 0xFF0E8A66, divider = 0xFFD2EDE4),
        palette("PRESET_MOCHA", R.string.theme_palette_mocha, bg = 0xFF1E1E2E, surface = 0xFF302D41, primary = 0xFFCBA6F7, accent = 0xFFF5C2E7, text = 0xFFCDD6F4, textSecondary = 0xFF9399B2, error = 0xFFF38BA8, cellFixed = 0xFFCDD6F4, cellEditable = 0xFF94E2D5, divider = 0xFF45475A),
        palette("PRESET_CRIMSON", R.string.theme_palette_crimson, bg = 0xFF2A1416, surface = 0xFF3A1C1F, primary = 0xFFE53E51, accent = 0xFFFF8A80, text = 0xFFFCE4E4, textSecondary = 0xFFC58F92, error = 0xFFFF5252, cellFixed = 0xFFFFC9C9, cellEditable = 0xFFEF6E78, divider = 0xFF512A2E),
        palette("PRESET_MIDNIGHT", R.string.theme_palette_midnight, bg = 0xFF101428, surface = 0xFF1A1F3A, primary = 0xFF5C6BC0, accent = 0xFF42A5F5, text = 0xFFE3E7FF, textSecondary = 0xFF8A92C4, error = 0xFFEF5350, cellFixed = 0xFFE3E7FF, cellEditable = 0xFF64B5F6, divider = 0xFF2A3158),
        palette("PRESET_SAND", R.string.theme_palette_sand, bg = 0xFFF5EFE2, surface = 0xFFFFFBF2, primary = 0xFF9A6E32, accent = 0xFF9A6E32, text = 0xFF4A3B28, textSecondary = 0xFF7E6E52, error = 0xFFC0584B, cellFixed = 0xFF4A3B28, cellEditable = 0xFF8A5E28, divider = 0xFFE3D6BC),
        palette("PRESET_LATTE", R.string.theme_palette_latte, bg = 0xFFEFF1F5, surface = 0xFFFFFFFF, primary = 0xFF1E66F5, accent = 0xFF8839EF, text = 0xFF4C4F69, textSecondary = 0xFF6C6F85, error = 0xFFD20F39, cellFixed = 0xFF4C4F69, cellEditable = 0xFF1E66F5, divider = 0xFFDCE0E8),
        palette("PRESET_EMERALD", R.string.theme_palette_emerald, bg = 0xFF0E2A22, surface = 0xFF143A2E, primary = 0xFF2ECC71, accent = 0xFF1ABC9C, text = 0xFFD7F5E6, textSecondary = 0xFF7BAE97, error = 0xFFE74C3C, cellFixed = 0xFFD7F5E6, cellEditable = 0xFF55D98A, divider = 0xFF1E5142),
        palette("PRESET_GRAPE", R.string.theme_palette_grape, bg = 0xFF211029, surface = 0xFF2E173A, primary = 0xFFA64DD6, accent = 0xFFCE93D8, text = 0xFFF0E1F7, textSecondary = 0xFFB18BC4, error = 0xFFEF5350, cellFixed = 0xFFF0E1F7, cellEditable = 0xFFC56BE8, divider = 0xFF3F2150),
        palette("PRESET_GRUVBOX", R.string.theme_palette_gruvbox, bg = 0xFF282828, surface = 0xFF3C3836, primary = 0xFFFE8019, accent = 0xFFB8BB26, text = 0xFFEBDBB2, textSecondary = 0xFFA89984, error = 0xFFFB4934, cellFixed = 0xFFEBDBB2, cellEditable = 0xFFFABD2F, divider = 0xFF504945),
        palette("PRESET_TOKYO_NIGHT", R.string.theme_palette_tokyo_night, bg = 0xFF1A1B26, surface = 0xFF24283B, primary = 0xFF7AA2F7, accent = 0xFFBB9AF7, text = 0xFFC0CAF5, textSecondary = 0xFF9AA5CE, error = 0xFFF7768E, cellFixed = 0xFFC0CAF5, cellEditable = 0xFF7DCFFF, divider = 0xFF292E42),
        palette("PRESET_EVERFOREST", R.string.theme_palette_everforest, bg = 0xFF2D353B, surface = 0xFF343F44, primary = 0xFFA7C080, accent = 0xFFDBBC7F, text = 0xFFD3C6AA, textSecondary = 0xFF9DA9A0, error = 0xFFE67E80, cellFixed = 0xFFD3C6AA, cellEditable = 0xFF83C092, divider = 0xFF4F585E),
        palette("PRESET_ROSE_PINE", R.string.theme_palette_rose_pine, bg = 0xFF232136, surface = 0xFF2A273F, primary = 0xFFC4A7E7, accent = 0xFFEBBCBA, text = 0xFFE0DEF4, textSecondary = 0xFF908CAA, error = 0xFFEB6F92, cellFixed = 0xFFE0DEF4, cellEditable = 0xFF9CCFD8, divider = 0xFF44415A),
        palette("PRESET_SLATE", R.string.theme_palette_slate, bg = 0xFFF2F4F6, surface = 0xFFFFFFFF, primary = 0xFF546E7A, accent = 0xFF78909C, text = 0xFF263238, textSecondary = 0xFF78909C, error = 0xFFE53935, cellFixed = 0xFF263238, cellEditable = 0xFF455A64, divider = 0xFFDDE3E6),
    )

    fun byId(id: String): ThemePalette? = all.firstOrNull { it.id == id }

    private fun palette(
        id: String,
        @StringRes labelRes: Int,
        bg: Long,
        surface: Long,
        primary: Long,
        accent: Long,
        text: Long,
        textSecondary: Long,
        error: Long,
        cellFixed: Long,
        cellEditable: Long,
        divider: Long,
    ): ThemePalette {
        val bgC = Color(bg)
        val surfaceC = Color(surface)
        val primaryC = Color(primary)
        val errorC = Color(error)
        val textC = Color(text)
        val isDark = bgC.luminance() < DARK_THRESHOLD
        val onPrimary = contrastText(primaryC)
        val primaryLight = if (isDark) lerp(primaryC, bgC, 0.70f).toArgbLong() else lighten(primaryC, 0.7f)
        return ThemePalette(
            id = id,
            labelRes = labelRes,
            isDark = isDark,
            colors = ThemeColors(
                primary = primary,
                primaryDark = darken(primaryC, 0.15f),
                primaryLight = primaryLight,
                secondary = surface,
                background = bg,
                backgroundCard = surface,
                backgroundCardAccent = primaryLight,
                surface = surface,
                text = text,
                textSecondary = textSecondary,
                textOnPrimary = onPrimary.toArgbLong(),
                textAccent = accent,
                error = error,
                errorLight = over(errorC.copy(alpha = 0.15f), surfaceC),
                success = accent,
                warning = WARNING,
                gridLine = lerp(surfaceC, textC, 0.22f).toArgbLong(),
                gridLineBold = lerp(surfaceC, textC, 0.45f).toArgbLong(),
                cellSelected = lerp(surfaceC, primaryC, 0.30f).toArgbLong(),
                cellHighlight = lerp(surfaceC, primaryC, 0.12f).toArgbLong(),
                cellSameNumber = lerp(surfaceC, primaryC, 0.22f).toArgbLong(),
                cellError = over(errorC.copy(alpha = 0.18f), surfaceC),
                cellFixed = cellFixed,
                cellEditable = cellEditable,
                draftText = textSecondary,
                draftHighlight = text,
                divider = divider,
                iconTint = textSecondary,
                bottomNavSelected = primary,
                bottomNavUnselected = textSecondary,
                chipSelected = primary,
                chipUnselected = lighten(surfaceC, 0.05f),
                chipTextSelected = onPrimary.toArgbLong(),
                chipTextUnselected = textSecondary,
                progressTrack = divider,
                progressIndicator = primary,
                barChart = primary,
                barChartLabel = textSecondary,
            ),
        )
    }

    private const val DARK_THRESHOLD = 0.5f
    private const val WARNING = 0xFFFF9500L

    private fun Color.toArgbLong(): Long = toArgb().toLong() and 0xFFFFFFFFL

    private fun darken(color: Color, fraction: Float): Long = lerp(color, Color.Black, fraction).toArgbLong()

    private fun lighten(color: Color, fraction: Float): Long = lerp(color, Color.White, fraction).toArgbLong()

    private fun over(foreground: Color, background: Color): Long {
        val a = foreground.alpha
        return Color(
            red = foreground.red * a + background.red * (1f - a),
            green = foreground.green * a + background.green * (1f - a),
            blue = foreground.blue * a + background.blue * (1f - a),
            alpha = 1f,
        ).toArgbLong()
    }

    private fun contrastText(background: Color): Color =
        if (background.luminance() > 0.5f) Color(0xFF1A1A1A) else Color(0xFFFFFFFF)
}
