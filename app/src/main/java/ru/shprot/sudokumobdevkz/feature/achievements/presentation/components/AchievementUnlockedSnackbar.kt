package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.icon.AppIcons

@Composable
internal fun AchievementUnlockedSnackbar(
    modifier: Modifier,
    message: String,
    onAction: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.primaryLight),
    ) {
        Row(
            modifier = Modifier.padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
        ) {
            Icon(
                imageVector = AppIcons.Trophy,
                contentDescription = null,
                tint = AppTheme.colors.primary,
                modifier = Modifier.size(AppTheme.sizes.iconLarge),
            )

            Text(
                modifier = Modifier.weight(1f),
                text = message,
                style = AppTheme.typography.body2,
                color = AppTheme.colors.text,
            )

            TextButton(onClick = onAction) {
                Text(
                    text = stringResource(R.string.achievement_unlocked_action),
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.button,
                )
            }
        }
    }
}