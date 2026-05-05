package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun TimeChartSection(
    modifier: Modifier,
    recentGames: List<GameHistoryEntity> = emptyList(),
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.time_dynamics),
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Card(
            modifier = Modifier.padding(top = AppTheme.paddings.default),
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            if (recentGames.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(AppTheme.paddings.large),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.no_data),
                        style = AppTheme.typography.body3,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            } else {
                TimeBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppTheme.paddings.large),
                    games = recentGames.reversed(),
                )
            }
        }
    }
}
