package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract

import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.contract.UIState

data class DailyChallengeUIState(
    val dateLabel: String = "",
    val difficulty: Difficulty = Difficulty.EASY,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val isCompletedToday: Boolean = false,
    val completionTimeSeconds: Int = 0,
    val errors: Int = 0,
    val isLoading: Boolean = true,
    val visibleMonthLabel: String = "",
    val weekDayLabels: List<String> = emptyList(),
    val firstDayOffset: Int = 0,
    val calendarDays: List<DayCellUiModel> = emptyList(),
    val monthCompletedCount: Int = 0,
    val monthTotalDays: Int = 0,
    val canGoPrev: Boolean = false,
    val canGoNext: Boolean = false,
) : UIState