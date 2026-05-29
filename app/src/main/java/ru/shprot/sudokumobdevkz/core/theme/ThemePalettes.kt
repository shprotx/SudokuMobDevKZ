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
        palette("PRESET_CLASSIC_LIGHT", R.string.theme_palette_classic_light, bg = 0xFFF8F9FA, surface = 0xFFFFFFFF, primary = 0xFF34C759, accent = 0xFF34C759, text = 0xFF1A1A1A, textSecondary = 0xFF8E8E93, error = 0xFFFF3B30, gridLine = 0xFF96B391, gridLineBold = 0xFF546E7A, cellFixed = 0xFF546E7A, cellEditable = 0xFF70856D, cellSelected = 0xFFB6DAB3, cellHighlight = 0xFFD6EBD2, divider = 0xFFE5E5EA),
        palette("PRESET_CLASSIC_DARK", R.string.theme_palette_classic_dark, bg = 0xFF1C1C1E, surface = 0xFF2C2C2E, primary = 0xFF30D158, accent = 0xFF30D158, text = 0xFFFFFFFF, textSecondary = 0xFF8E8E93, error = 0xFFFF453A, gridLine = 0xFF58585A, gridLineBold = 0xFFD0D0D0, cellFixed = 0xFFE5E5E7, cellEditable = 0xFFE0B973, cellSelected = 0xFF3A3A3C, cellHighlight = 0xFF333335, divider = 0xFF48484A),
        palette("PRESET_SOLARIZED_LIGHT", R.string.theme_palette_solarized_light, bg = 0xFFFDF6E3, surface = 0xFFEEE8D5, primary = 0xFF268BD2, accent = 0xFF859900, text = 0xFF657B83, textSecondary = 0xFF93A1A1, error = 0xFFDC322F, gridLine = 0xFFC0B49A, gridLineBold = 0xFF839496, cellFixed = 0xFF586E75, cellEditable = 0xFF268BD2, cellSelected = 0xFFBDD6E9, cellHighlight = 0xFFD6E8F4, divider = 0xFFD3C9B2),
        palette("PRESET_SOLARIZED_DARK", R.string.theme_palette_solarized_dark, bg = 0xFF002B36, surface = 0xFF073642, primary = 0xFF268BD2, accent = 0xFFB58900, text = 0xFF93A1A1, textSecondary = 0xFF657B83, error = 0xFFDC322F, gridLine = 0xFF0E4D5E, gridLineBold = 0xFF839496, cellFixed = 0xFFEEE8D5, cellEditable = 0xFF2AA198, cellSelected = 0xFF0E4D5E, cellHighlight = 0xFF0B3D4A, divider = 0xFF0E4D5E),
        palette("PRESET_SAKURA", R.string.theme_palette_sakura, bg = 0xFFFFF5F7, surface = 0xFFFFFFFF, primary = 0xFFEC85A8, accent = 0xFFD17A98, text = 0xFF4A2733, textSecondary = 0xFF9E7A86, error = 0xFFE53935, gridLine = 0xFFF3C6D3, gridLineBold = 0xFFD17A98, cellFixed = 0xFF6D3B4E, cellEditable = 0xFFE0719B, cellSelected = 0xFFFAD1DE, cellHighlight = 0xFFFDE9EF, divider = 0xFFF6E2E8),
        palette("PRESET_FOREST", R.string.theme_palette_forest, bg = 0xFF1A2416, surface = 0xFF243020, primary = 0xFF4CAF50, accent = 0xFFAED581, text = 0xFFB5CC90, textSecondary = 0xFF7A9965, error = 0xFFEF5350, gridLine = 0xFF3C5234, gridLineBold = 0xFF8AAE72, cellFixed = 0xFFAED581, cellEditable = 0xFF81C784, cellSelected = 0xFF2E5C2E, cellHighlight = 0xFF1E4020, divider = 0xFF2E3D28),
        palette("PRESET_DRACULA", R.string.theme_palette_dracula, bg = 0xFF282A36, surface = 0xFF343746, primary = 0xFFBD93F9, accent = 0xFFFF79C6, text = 0xFFF8F8F2, textSecondary = 0xFF9CA0B0, error = 0xFFFF5555, gridLine = 0xFF44475A, gridLineBold = 0xFF6272A4, cellFixed = 0xFFF8F8F2, cellEditable = 0xFF8BE9FD, cellSelected = 0xFF44475A, cellHighlight = 0xFF3C3F51, divider = 0xFF44475A),
        palette("PRESET_NORD", R.string.theme_palette_nord, bg = 0xFF2E3440, surface = 0xFF3B4252, primary = 0xFF88C0D0, accent = 0xFFA3BE8C, text = 0xFFECEFF4, textSecondary = 0xFF9BA3B4, error = 0xFFBF616A, gridLine = 0xFF434C5E, gridLineBold = 0xFF81A1C1, cellFixed = 0xFFECEFF4, cellEditable = 0xFF8FBCBB, cellSelected = 0xFF434C5E, cellHighlight = 0xFF3B4252, divider = 0xFF434C5E),
        palette("PRESET_OCEAN", R.string.theme_palette_ocean, bg = 0xFFEBF5FB, surface = 0xFFFFFFFF, primary = 0xFF0277BD, accent = 0xFF00ACC1, text = 0xFF0D3B53, textSecondary = 0xFF5A8199, error = 0xFFE53935, gridLine = 0xFFAED4E6, gridLineBold = 0xFF4A88A8, cellFixed = 0xFF0D3B53, cellEditable = 0xFF0288D1, cellSelected = 0xFFB3E0F2, cellHighlight = 0xFFD6EEF7, divider = 0xFFCDE5EF),
        palette("PRESET_SUNSET", R.string.theme_palette_sunset, bg = 0xFF2B1B2D, surface = 0xFF3A2438, primary = 0xFFFF8A5C, accent = 0xFFFFB74D, text = 0xFFFFE9DD, textSecondary = 0xFFC79BA8, error = 0xFFFF5252, gridLine = 0xFF4E3450, gridLineBold = 0xFFE07A5F, cellFixed = 0xFFFFD9B3, cellEditable = 0xFFFFAB76, cellSelected = 0xFF4E3450, cellHighlight = 0xFF3E2A40, divider = 0xFF4E3450),
        palette("PRESET_LAVENDER", R.string.theme_palette_lavender, bg = 0xFFF5F3FB, surface = 0xFFFFFFFF, primary = 0xFF7E57C2, accent = 0xFF9575CD, text = 0xFF2E2640, textSecondary = 0xFF847B9E, error = 0xFFE53935, gridLine = 0xFFD6CCEB, gridLineBold = 0xFF8A77B5, cellFixed = 0xFF4A3B73, cellEditable = 0xFF7E57C2, cellSelected = 0xFFE0D6F5, cellHighlight = 0xFFEDE7F8, divider = 0xFFE3DCF0),
        palette("PRESET_MINT", R.string.theme_palette_mint, bg = 0xFFEFFAF5, surface = 0xFFFFFFFF, primary = 0xFF1DBF8E, accent = 0xFF26C6DA, text = 0xFF13413A, textSecondary = 0xFF5E8C82, error = 0xFFE53935, gridLine = 0xFFB8E6D6, gridLineBold = 0xFF4FA890, cellFixed = 0xFF13413A, cellEditable = 0xFF15A87B, cellSelected = 0xFFBDEBDC, cellHighlight = 0xFFD9F4EB, divider = 0xFFD2EDE4),
        palette("PRESET_MOCHA", R.string.theme_palette_mocha, bg = 0xFF1E1E2E, surface = 0xFF302D41, primary = 0xFFCBA6F7, accent = 0xFFF5C2E7, text = 0xFFCDD6F4, textSecondary = 0xFF9399B2, error = 0xFFF38BA8, gridLine = 0xFF45475A, gridLineBold = 0xFF89B4FA, cellFixed = 0xFFCDD6F4, cellEditable = 0xFF94E2D5, cellSelected = 0xFF45475A, cellHighlight = 0xFF3B3950, divider = 0xFF45475A),
        palette("PRESET_CRIMSON", R.string.theme_palette_crimson, bg = 0xFF2A1416, surface = 0xFF3A1C1F, primary = 0xFFE53E51, accent = 0xFFFF8A80, text = 0xFFFCE4E4, textSecondary = 0xFFC58F92, error = 0xFFFF5252, gridLine = 0xFF512A2E, gridLineBold = 0xFFD96570, cellFixed = 0xFFFFC9C9, cellEditable = 0xFFEF6E78, cellSelected = 0xFF512A2E, cellHighlight = 0xFF421F23, divider = 0xFF512A2E),
        palette("PRESET_MIDNIGHT", R.string.theme_palette_midnight, bg = 0xFF101428, surface = 0xFF1A1F3A, primary = 0xFF5C6BC0, accent = 0xFF42A5F5, text = 0xFFE3E7FF, textSecondary = 0xFF8A92C4, error = 0xFFEF5350, gridLine = 0xFF2A3158, gridLineBold = 0xFF7986CB, cellFixed = 0xFFE3E7FF, cellEditable = 0xFF64B5F6, cellSelected = 0xFF2A3158, cellHighlight = 0xFF20264A, divider = 0xFF2A3158),
        palette("PRESET_SAND", R.string.theme_palette_sand, bg = 0xFFF5EFE2, surface = 0xFFFFFBF2, primary = 0xFFC2925A, accent = 0xFFD4A373, text = 0xFF4A3B28, textSecondary = 0xFF94836A, error = 0xFFC0584B, gridLine = 0xFFDCCBAE, gridLineBold = 0xFFA8895E, cellFixed = 0xFF4A3B28, cellEditable = 0xFFB07D3E, cellSelected = 0xFFE8D8BC, cellHighlight = 0xFFF1E8D4, divider = 0xFFE3D6BC),
        palette("PRESET_CORAL", R.string.theme_palette_coral, bg = 0xFFFFF4F0, surface = 0xFFFFFFFF, primary = 0xFFFF6F61, accent = 0xFFFF9478, text = 0xFF4A2A26, textSecondary = 0xFFA8786F, error = 0xFFE53935, gridLine = 0xFFFAD2C8, gridLineBold = 0xFFE08071, cellFixed = 0xFF4A2A26, cellEditable = 0xFFF96A5A, cellSelected = 0xFFFFD6CC, cellHighlight = 0xFFFFE8E2, divider = 0xFFFAE0D9),
        palette("PRESET_EMERALD", R.string.theme_palette_emerald, bg = 0xFF0E2A22, surface = 0xFF143A2E, primary = 0xFF2ECC71, accent = 0xFF1ABC9C, text = 0xFFD7F5E6, textSecondary = 0xFF7BAE97, error = 0xFFE74C3C, gridLine = 0xFF1E5142, gridLineBold = 0xFF52C98F, cellFixed = 0xFFD7F5E6, cellEditable = 0xFF55D98A, cellSelected = 0xFF1E5142, cellHighlight = 0xFF164036, divider = 0xFF1E5142),
        palette("PRESET_GRAPE", R.string.theme_palette_grape, bg = 0xFF211029, surface = 0xFF2E173A, primary = 0xFFA64DD6, accent = 0xFFCE93D8, text = 0xFFF0E1F7, textSecondary = 0xFFB18BC4, error = 0xFFEF5350, gridLine = 0xFF3F2150, gridLineBold = 0xFFB968E0, cellFixed = 0xFFF0E1F7, cellEditable = 0xFFC56BE8, cellSelected = 0xFF3F2150, cellHighlight = 0xFF321843, divider = 0xFF3F2150),
        palette("PRESET_SLATE", R.string.theme_palette_slate, bg = 0xFFF2F4F6, surface = 0xFFFFFFFF, primary = 0xFF546E7A, accent = 0xFF78909C, text = 0xFF263238, textSecondary = 0xFF78909C, error = 0xFFE53935, gridLine = 0xFFCFD8DC, gridLineBold = 0xFF607D8B, cellFixed = 0xFF263238, cellEditable = 0xFF455A64, cellSelected = 0xFFD6E0E4, cellHighlight = 0xFFE8EDEF, divider = 0xFFDDE3E6),
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
        gridLine: Long,
        gridLineBold: Long,
        cellFixed: Long,
        cellEditable: Long,
        cellSelected: Long,
        cellHighlight: Long,
        divider: Long,
    ): ThemePalette {
        val bgC = Color(bg)
        val surfaceC = Color(surface)
        val primaryC = Color(primary)
        val errorC = Color(error)
        val onPrimary = contrastText(primaryC)
        return ThemePalette(
            id = id,
            labelRes = labelRes,
            isDark = bgC.luminance() < DARK_THRESHOLD,
            colors = ThemeColors(
                primary = primary,
                primaryDark = darken(primaryC, 0.15f),
                primaryLight = lighten(primaryC, 0.7f),
                secondary = surface,
                background = bg,
                backgroundCard = surface,
                backgroundCardAccent = lighten(surfaceC, 0.06f),
                surface = surface,
                text = text,
                textSecondary = textSecondary,
                textOnPrimary = onPrimary.toArgbLong(),
                textAccent = accent,
                error = error,
                errorLight = over(errorC.copy(alpha = 0.15f), surfaceC),
                success = accent,
                warning = WARNING,
                gridLine = gridLine,
                gridLineBold = gridLineBold,
                cellSelected = cellSelected,
                cellHighlight = cellHighlight,
                cellSameNumber = lerp(Color(cellHighlight), primaryC, 0.18f).toArgbLong(),
                cellError = over(errorC.copy(alpha = 0.18f), surfaceC),
                cellFixed = cellFixed,
                cellEditable = cellEditable,
                draftText = textSecondary,
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
