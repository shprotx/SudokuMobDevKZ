package ru.shprot.sudokumobdevkz.core.base.domain.model

data class VisitStreak(
    val currentStreak: Int = 0,
    val bestStreak: Int = 0,
    val lastVisitDate: String? = null,
)