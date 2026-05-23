package ru.shprot.sudokumobdevkz.feature.settings.presentation.components.cloud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.cloud.model.CloudProgress
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonDefault
import ru.shprot.sudokumobdevkz.core.uicommon.button.ButtonOutlined

@Composable
fun CloudImportDialog(
    local: CloudProgress,
    cloud: CloudProgress,
    onMerge: () -> Unit,
    onKeepLocal: () -> Unit,
    onUseCloud: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.paddings.xxl),
            color = AppTheme.colors.backgroundCard,
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
        ) {

            Column(
                modifier = Modifier.padding(AppTheme.paddings.xxl),
                verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default),
            ) {

                Text(
                    text = stringResource(R.string.cloud_import_title),
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.text,
                )

                Text(
                    text = stringResource(R.string.cloud_import_message),
                    style = AppTheme.typography.body2,
                    color = AppTheme.colors.textSecondary,
                )

                Text(
                    text = stringResource(
                        R.string.cloud_import_summary,
                        statsSummary(local),
                        statsSummary(cloud),
                    ),
                    style = AppTheme.typography.body3,
                    color = AppTheme.colors.textSecondary,
                )

                Column(
                    modifier = Modifier.padding(top = AppTheme.paddings.medium),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
                ) {

                    ButtonDefault(
                        modifier = Modifier,
                        text = stringResource(R.string.cloud_import_merge),
                        onClick = onMerge,
                    )

                    ButtonOutlined(
                        modifier = Modifier,
                        text = stringResource(R.string.cloud_import_use_cloud),
                        onClick = onUseCloud,
                    )

                    ButtonOutlined(
                        modifier = Modifier,
                        text = stringResource(R.string.cloud_import_keep_local),
                        borderColor = AppTheme.colors.textSecondary,
                        textColor = AppTheme.colors.textSecondary,
                        onClick = onKeepLocal,
                    )
                }
            }
        }
    }
}

private fun statsSummary(progress: CloudProgress): String {
    val totalWins = progress.statistics.values.sumOf { it.gamesWon }
    val unlocked = progress.unlockedAchievements.size
    val dailies = progress.dailyChallenges.count { it.isCompleted }
    return "$totalWins / $unlocked / $dailies"
}
