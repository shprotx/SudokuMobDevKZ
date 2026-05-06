package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementCard(
    modifier: Modifier,
    state: AchievementState,
) {
    val isUnlocked = state.unlockedAt != null
    val isHidden = state.achievement.hidden && !isUnlocked

    val titleRes = if (isHidden) R.string.achievement_secret_title else state.achievement.titleRes
    val descRes = if (isHidden) R.string.achievement_secret_desc else state.achievement.descRes

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.surface),
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.large),
        ) {
            Box(modifier = Modifier.size(AppTheme.sizes.iconXL)) {
                when {
                    isHidden -> SecretAchievementIcon(modifier = Modifier.fillMaxWidth())
                    isUnlocked -> AchievementIcon(
                        modifier = Modifier.fillMaxWidth(),
                        iconKey = state.achievement.iconKey,
                    )
                    else -> LockedAchievementIcon(
                        modifier = Modifier.fillMaxWidth(),
                        iconKey = state.achievement.iconKey,
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.extraSmall),
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(titleRes),
                        style = AppTheme.typography.h3,
                        color = AppTheme.colors.text,
                    )

                    if (isUnlocked) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = AppTheme.colors.success,
                            modifier = Modifier.size(AppTheme.sizes.iconSmall),
                        )
                    }
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraSmall),
                    text = stringResource(descRes),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                val showProgress = !isUnlocked && !isHidden && state.progress.target > 1
                if (showProgress) {
                    AchievementProgressBar(
                        modifier = Modifier.padding(top = AppTheme.paddings.medium),
                        current = state.progress.current,
                        target = state.progress.target,
                    )
                }
            }
        }
    }
}