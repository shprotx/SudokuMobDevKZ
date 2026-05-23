package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun CloudImportDialog(
    local: CloudProgress,
    cloud: CloudProgress,
    onMerge: () -> Unit,
    onKeepLocal: () -> Unit,
    onUseCloud: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.cloud_import_title),
                style = AppTheme.typography.h3,
                color = AppTheme.colors.text,
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(R.string.cloud_import_message),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                )
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.default),
                    text = stringResource(
                        R.string.cloud_import_summary,
                        statsSummary(local),
                        statsSummary(cloud),
                    ),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onMerge) {
                Text(
                    text = stringResource(R.string.cloud_import_merge),
                    color = AppTheme.colors.primary,
                )
            }
        },
        dismissButton = {
            Column {
                TextButton(onClick = onKeepLocal) {
                    Text(
                        text = stringResource(R.string.cloud_import_keep_local),
                        color = AppTheme.colors.textSecondary,
                    )
                }
                TextButton(onClick = onUseCloud) {
                    Text(
                        text = stringResource(R.string.cloud_import_use_cloud),
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }
        },
        containerColor = AppTheme.colors.backgroundCard,
    )
}

private fun statsSummary(progress: CloudProgress): String {
    val totalWins = progress.statistics.values.sumOf { it.gamesWon }
    val unlocked = progress.unlockedAchievements.size
    val dailies = progress.dailyChallenges.count { it.isCompleted }
    return "$totalWins / $unlocked / $dailies"
}
