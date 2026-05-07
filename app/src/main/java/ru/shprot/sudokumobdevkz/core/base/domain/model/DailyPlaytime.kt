package ru.shprot.sudokumobdevkz.core.base.domain.model

import java.time.LocalDate

data class DailyPlaytime(
    val date: LocalDate,
    val totalSeconds: Int,
)