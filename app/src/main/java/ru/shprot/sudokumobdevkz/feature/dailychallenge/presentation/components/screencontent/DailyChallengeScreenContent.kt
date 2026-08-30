package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.util.DateTimeUtils
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.DailyCalendar
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.DailyHeroCard
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.DailyResultCard
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.StreakBadge
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEvent
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIState

@Composable
fun DailyChallengeScreenContent(
    uiState: DailyChallengeUIState,
    onEvent: (DailyChallengeUIEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.daily_challenge),
            onLeadIconClick = { onEvent(DailyChallengeUIEvent.BackClicked) },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = AppTheme.paddings.large),
        ) {
            DailyHeroCard(
                modifier = Modifier.padding(top = AppTheme.paddings.default),
                dateLabel = uiState.dateLabel,
                difficultyTitle = stringResource(uiState.difficulty.titleRes),
                difficultyEmoji = uiState.difficulty.emoji,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = AppTheme.paddings.default),
                horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {
                StreakBadge(
                    modifier = Modifier.weight(1f),
                    streak = uiState.currentStreak,
                    label = stringResource(R.string.daily_streak_label),
                    icon = AppIcons.Fire,
                    iconTint = AppTheme.colors.warning,
                )

                StreakBadge(
                    modifier = Modifier.weight(1f),
                    streak = uiState.longestStreak,
                    label = stringResource(R.string.daily_best_streak_label),
                    icon = AppIcons.Medal,
                    iconTint = AppTheme.colors.primary,
                )
            }

            if (uiState.isCompletedToday) {
                DailyResultCard(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    title = stringResource(R.string.daily_completed),
                    timeLabel = stringResource(R.string.time_label),
                    timeValue = DateTimeUtils.formatTimer(uiState.completionTimeSeconds),
                    errorsLabel = stringResource(R.string.errors_label),
                    errorsValue = uiState.errors.toString(),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = AppTheme.paddings.default,
                        bottom = AppTheme.paddings.default,
                    ),
                verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {
                if (!uiState.isCompletedToday) {
                    ButtonDefault(
                        modifier = Modifier,
                        text = stringResource(R.string.daily_play),
                        onClick = { onEvent(DailyChallengeUIEvent.PlayClicked) },
                    )
                }
            }

            if (uiState.calendarDays.isNotEmpty()) {
                DailyCalendar(
                    modifier = Modifier.padding(bottom = AppTheme.paddings.large),
                    visibleMonthLabel = uiState.visibleMonthLabel,
                    weekDayLabels = uiState.weekDayLabels,
                    firstDayOffset = uiState.firstDayOffset,
                    calendarDays = uiState.calendarDays,
                    monthCompletedCount = uiState.monthCompletedCount,
                    monthTotalDays = uiState.monthTotalDays,
                    canGoPrev = uiState.canGoPrev,
                    canGoNext = uiState.canGoNext,
                    onPrevMonth = { onEvent(DailyChallengeUIEvent.PrevMonth) },
                    onNextMonth = { onEvent(DailyChallengeUIEvent.NextMonth) },
                    onDayClicked = { dateKey -> onEvent(DailyChallengeUIEvent.DayClicked(dateKey)) },
                )
            }
        }
    }
}