package ru.shprot.sudokumobdevkz.feature.achievements.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
internal fun AchievementProgressBar(
    modifier: Modifier,
    current: Int,
    target: Int,
) {
    val ratio = if (target <= 0) 0f else (current.toFloat() / target).coerceIn(0f, 1f)
    Column(modifier = modifier) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = { ratio },
            color = AppTheme.colors.progressIndicator,
            trackColor = AppTheme.colors.progressTrack,
        )

        Text(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = AppTheme.paddings.extraSmall),
            text = "$current / $target",
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )
    }
}