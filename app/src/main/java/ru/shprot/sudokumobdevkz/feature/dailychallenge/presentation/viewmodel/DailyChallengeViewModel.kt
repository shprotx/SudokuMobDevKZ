package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.base.presentation.viewmodel.BaseViewModel
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DayCellState
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DayCellUiModel
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEffect
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEvent
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIState
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DailyChallengeViewModel @Inject constructor(
    private val repository: DailyChallengeRepository,
) : BaseViewModel<DailyChallengeUIEvent, DailyChallengeUIState, DailyChallengeUIEffect>(
    DailyChallengeUIState(),
) {

    private var completedDates: Set<LocalDate> = emptySet()
    private var visibleYearMonth: YearMonth = YearMonth.now()
    private var earliestMonth: YearMonth = YearMonth.now()

    init {
        loadChallenge()
        observeCalendar()
    }

    override fun handleUIEvent(event: DailyChallengeUIEvent) =
        when (event) {
            DailyChallengeUIEvent.BackClicked ->
                setEffect(DailyChallengeUIEffect.NavigateBack)

            DailyChallengeUIEvent.PlayClicked ->
                handlePlayClicked()

            DailyChallengeUIEvent.PrevMonth ->
                handlePrevMonth()

            DailyChallengeUIEvent.NextMonth ->
                handleNextMonth()

            is DailyChallengeUIEvent.DayClicked ->
                handleDayClicked(event.dateKey)
        }

    private fun handlePlayClicked() {
        if (currentState.isCompletedToday) return
        val todayKey = repository.todayDateKey()
        setEffect(
            DailyChallengeUIEffect.NavigateToGame(
                difficultyOrdinal = currentState.difficulty.ordinal,
                dailyDateKey = todayKey,
            )
        )
    }

    private fun handlePrevMonth() {
        if (visibleYearMonth <= earliestMonth) return
        visibleYearMonth = visibleYearMonth.minusMonths(1)
        rebuildCalendar()
    }

    private fun handleNextMonth() {
        if (visibleYearMonth >= YearMonth.now()) return
        visibleYearMonth = visibleYearMonth.plusMonths(1)
        rebuildCalendar()
    }

    private fun handleDayClicked(dateKey: String) {
        val date = LocalDate.parse(dateKey)
        if (date.isAfter(LocalDate.now())) return
        val difficulty = repository.difficultyForDate(dateKey)
        setEffect(
            DailyChallengeUIEffect.NavigateToGame(
                difficultyOrdinal = difficulty.ordinal,
                dailyDateKey = dateKey,
            )
        )
    }

    private fun loadChallenge() {
        viewModelScope.launch(exceptionHandler) {
            val challenge = repository.getTodayChallenge()
            val current = repository.getCurrentStreak()
            val longest = repository.getLongestStreak()
            updateState {
                copy(
                    dateLabel = DateTimeUtils.formatLocalizedDate(challenge.dateKey),
                    difficulty = Difficulty.fromOrdinal(challenge.difficultyOrdinal),
                    currentStreak = current,
                    longestStreak = maxOf(longest, current),
                    isCompletedToday = challenge.isCompleted,
                    completionTimeSeconds = challenge.completionTimeSeconds,
                    errors = challenge.errors,
                    isLoading = false,
                )
            }
        }
    }

    private fun observeCalendar() {
        viewModelScope.launch(exceptionHandler) {
            repository.observeAllCompleted().collect { completed ->
                completedDates = completed.mapTo(mutableSetOf()) { LocalDate.parse(it.dateKey) }
                earliestMonth = completedDates.minOrNull()
                    ?.let { YearMonth.from(it) }
                    ?.takeIf { it.isBefore(YearMonth.now()) }
                    ?: YearMonth.now()
                rebuildCalendar()
            }
        }
    }

    private fun rebuildCalendar() {
        val today = LocalDate.now()
        val todayYearMonth = YearMonth.now()
        val daysInMonth = visibleYearMonth.lengthOfMonth()
        val firstDayOffset = computeFirstDayOffset()
        val weekDayLabels = computeWeekDayLabels()

        val cells = (1..daysInMonth).map { dayNum ->
            val date = visibleYearMonth.atDay(dayNum)
            val dateKey = date.toString()
            val state = when {
                date.isAfter(today) -> DayCellState.Future
                completedDates.contains(date) -> DayCellState.Completed(repository.difficultyForDate(dateKey))
                date == today -> DayCellState.Today
                else -> DayCellState.Missed
            }
            DayCellUiModel(
                dateKey = dateKey,
                dayNumber = dayNum,
                state = state,
            )
        }

        val monthCompletedCount = cells.count { it.state is DayCellState.Completed }

        updateState {
            copy(
                visibleMonthLabel = computeMonthLabel(),
                weekDayLabels = weekDayLabels,
                firstDayOffset = firstDayOffset,
                calendarDays = cells,
                monthCompletedCount = monthCompletedCount,
                monthTotalDays = daysInMonth,
                canGoPrev = visibleYearMonth > earliestMonth,
                canGoNext = visibleYearMonth < todayYearMonth,
            )
        }
    }

    private fun computeFirstDayOffset(): Int {
        val firstDay = visibleYearMonth.atDay(1)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        return ((firstDay.dayOfWeek.value - firstDayOfWeek.value + 7) % 7)
    }

    private fun computeWeekDayLabels(): List<String> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        return (0 until 7).map { i ->
            val dow = DayOfWeek.of(((firstDayOfWeek.value - 1 + i) % 7) + 1)
            dow.getDisplayName(TextStyle.NARROW, Locale.getDefault())
        }
    }

    private fun computeMonthLabel(): String {
        val formatter = DateTimeFormatter.ofPattern("LLLL yyyy", Locale.getDefault())
        return visibleYearMonth.format(formatter).replaceFirstChar { it.uppercaseChar() }
    }
}