package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementCard
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementsHeader
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementsSectionTitle
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIEvent
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIState

@Composable
fun AchievementsScreenContent(
    modifier: Modifier,
    uiState: AchievementsUIState,
    onEvent: (AchievementsUIEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(AppTheme.paddings.large),
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
        ) {
            item {
                AchievementsHeader(
                    modifier = Modifier,
                    unlocked = uiState.totalUnlocked,
                    total = uiState.totalCount,
                )
            }

            if (uiState.unlocked.isNotEmpty()) {
                item {
                    AchievementsSectionTitle(
                        modifier = Modifier,
                        titleRes = R.string.achievements_section_unlocked,
                        count = uiState.unlocked.size,
                    )
                }
                items(items = uiState.unlocked, key = { it.achievement.id }) { state ->
                    AchievementCard(modifier = Modifier, state = state)
                }
            }

            if (uiState.inProgress.isNotEmpty()) {
                item {
                    AchievementsSectionTitle(
                        modifier = Modifier,
                        titleRes = R.string.achievements_section_in_progress,
                        count = uiState.inProgress.size,
                    )
                }
                items(items = uiState.inProgress, key = { it.achievement.id }) { state ->
                    AchievementCard(modifier = Modifier, state = state)
                }
            }

            if (uiState.locked.isNotEmpty()) {
                item {
                    AchievementsSectionTitle(
                        modifier = Modifier,
                        titleRes = R.string.achievements_section_locked,
                        count = uiState.locked.size,
                    )
                }
                items(items = uiState.locked, key = { it.achievement.id }) { state ->
                    AchievementCard(modifier = Modifier, state = state)
                }
            }
        }
    }
}