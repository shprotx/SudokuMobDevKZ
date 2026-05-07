package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.Text
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.achievement.AchievementState
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault

private val DialogIconSize = 140.dp

@Composable
internal fun AchievementDetailDialog(
    state: AchievementState,
    onDismiss: () -> Unit,
) {
    val isUnlocked = state.unlockedAt != null
    val isHidden = state.achievement.hidden && !isUnlocked

    val titleRes = if (isHidden) R.string.achievement_secret_title else state.achievement.titleRes
    val descRes = if (isHidden) R.string.achievement_secret_desc else state.achievement.descRes

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.paddings.large)
                .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusXL))
                .background(AppTheme.colors.surface)
                .padding(
                    horizontal = AppTheme.paddings.large,
                    vertical = AppTheme.paddings.xxl,
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when {
                    isHidden -> SecretAchievementIcon(
                        modifier = Modifier,
                        size = DialogIconSize,
                    )

                    isUnlocked -> AchievementIcon(
                        modifier = Modifier,
                        iconKey = state.achievement.iconKey,
                        size = DialogIconSize,
                    )

                    else -> LockedAchievementIcon(
                        modifier = Modifier,
                        iconKey = state.achievement.iconKey,
                        size = DialogIconSize,
                    )
                }

                val badgeVariant = when {
                    isUnlocked -> AchievementBadgeVariant.UNLOCKED
                    isHidden -> AchievementBadgeVariant.SECRET
                    else -> null
                }
                badgeVariant?.let { variant ->
                    AchievementBadge(
                        modifier = Modifier.padding(top = AppTheme.paddings.large),
                        variant = variant,
                    )
                }

                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.large),
                    text = stringResource(titleRes),
                    style = AppTheme.typography.h2,
                    fontWeight = FontWeight.Bold,
                    color = AppTheme.colors.text,
                    textAlign = TextAlign.Center,
                )

                Text(
                    modifier = Modifier
                        .padding(top = AppTheme.paddings.medium)
                        .fillMaxWidth(),
                    text = stringResource(descRes),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )

                if (!isUnlocked && !isHidden && state.progress.target > 1) {
                    AchievementProgressBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = AppTheme.paddings.xxl),
                        current = state.progress.current,
                        target = state.progress.target,
                    )
                }

                ButtonDefault(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppTheme.paddings.xxl),
                    text = stringResource(R.string.ok),
                    onClick = onDismiss,
                )
            }
        }
    }
}
