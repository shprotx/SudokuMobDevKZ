package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementGridTile(
    modifier: Modifier,
    state: AchievementState,
    iconSize: Dp,
    onClick: () -> Unit,
) {
    val isUnlocked = state.unlockedAt != null
    val isHidden = state.achievement.hidden && !isUnlocked

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = AppTheme.paddings.small),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(contentAlignment = Alignment.Center) {
            when {
                isHidden -> SecretAchievementIcon(modifier = Modifier, size = iconSize)
                isUnlocked -> AchievementIcon(
                    modifier = Modifier,
                    iconKey = state.achievement.iconKey,
                    size = iconSize,
                )
                else -> LockedAchievementIcon(
                    modifier = Modifier,
                    iconKey = state.achievement.iconKey,
                    size = iconSize,
                )
            }
        }

        if (!isUnlocked && !isHidden) {
            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = "${state.progress.current} / ${state.progress.target}",
                style = AppTheme.typography.caption2,
                color = AppTheme.colors.textSecondary,
                textAlign = TextAlign.Center,
            )
        }
    }
}
