package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.screencontent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.toolbar.ToolbarDefault
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementDetailDialog
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementGridTile
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementsHeader
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.AchievementsSectionTitle
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIEvent
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIState

private val GridIconSize = 92.dp

@Composable
fun AchievementsScreenContent(
    modifier: Modifier,
    uiState: AchievementsUIState,
    onEvent: (AchievementsUIEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
    ) {
        ToolbarDefault(
            modifier = Modifier,
            title = stringResource(R.string.achievements),
            onLeadIconClick = { onEvent(AchievementsUIEvent.BackClicked) },
            onEndIconClick = { onEvent(AchievementsUIEvent.SettingsClicked) },
        )

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(AppTheme.paddings.large),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                AchievementsHeader(
                    modifier = Modifier.fillMaxWidth(),
                    unlocked = uiState.totalUnlocked,
                    total = uiState.totalCount,
                )
            }

            if (uiState.unlocked.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AchievementsSectionTitle(
                        modifier = Modifier.padding(top = AppTheme.paddings.medium),
                        titleRes = R.string.achievements_section_unlocked,
                        count = uiState.unlocked.size,
                    )
                }
                items(items = uiState.unlocked, key = { it.achievement.id }) { state ->
                    AchievementGridTile(
                        modifier = Modifier,
                        state = state,
                        iconSize = GridIconSize,
                        onClick = { onEvent(AchievementsUIEvent.AchievementClicked(state)) },
                    )
                }
            }

            if (uiState.locked.isNotEmpty()) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AchievementsSectionTitle(
                        modifier = Modifier.padding(top = AppTheme.paddings.medium),
                        titleRes = R.string.achievements_section_locked,
                        count = uiState.locked.size,
                    )
                }
                items(items = uiState.locked, key = { it.achievement.id }) { state ->
                    AchievementGridTile(
                        modifier = Modifier,
                        state = state,
                        iconSize = GridIconSize,
                        onClick = { onEvent(AchievementsUIEvent.AchievementClicked(state)) },
                    )
                }
            }
        }
    }

    uiState.selected?.let { selected ->
        AchievementDetailDialog(
            state = selected,
            onDismiss = { onEvent(AchievementsUIEvent.DismissDialog) },
        )
    }
}
