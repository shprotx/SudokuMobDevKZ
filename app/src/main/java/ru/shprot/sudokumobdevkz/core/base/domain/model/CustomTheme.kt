package ru.shprot.sudokumobdevkz.core.base.domain.model

data class CustomTheme(
    val id: String,
    val name: String,
    val isBuiltIn: Boolean,
    val colors: ThemeColors,
    val createdAt: Long,
)

val CustomTheme.isDark: Boolean
    get() {
        val background = colors.background
        val red = ((background shr 16) and 0xFF) / 255.0
        val green = ((background shr 8) and 0xFF) / 255.0
        val blue = (background and 0xFF) / 255.0
        return (0.2126 * red + 0.7152 * green + 0.0722 * blue) < 0.5
    }