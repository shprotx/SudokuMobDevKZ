package ru.shprot.sudokumobdevkz.core.base.domain.model

sealed interface ThemeMode {
    val id: String

    data object System : ThemeMode {
        override val id = "SYSTEM"
    }

    data object Light : ThemeMode {
        override val id = "LIGHT"
    }

    data object Dark : ThemeMode {
        override val id = "DARK"
    }

    data class Custom(
        override val id: String,
        val title: String,
        val isDark: Boolean,
    ) : ThemeMode

    companion object {
        fun builtIn(): List<ThemeMode> = listOf(System, Light, Dark)

        fun fromId(id: String?): ThemeMode = when (id) {
            System.id -> System
            Light.id -> Light
            Dark.id -> Dark
            else -> System
        }
    }
}

fun ThemeMode.resolveDark(isSystemDark: Boolean): Boolean = when (this) {
    ThemeMode.System -> isSystemDark
    ThemeMode.Light -> false
    ThemeMode.Dark -> true
    is ThemeMode.Custom -> isDark
}