package ru.shprot.sudokumobdevkz.core.base.domain.model

data class CustomTheme(
    val id: String,
    val name: String,
    val isBuiltIn: Boolean,
    val colors: ThemeColors,
    val createdAt: Long,
)