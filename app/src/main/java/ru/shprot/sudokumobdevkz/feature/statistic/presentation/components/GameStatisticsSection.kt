package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun GameStatisticsSection(
    modifier: Modifier,
    gamesStarted: String,
    gamesWon: String,
    percentOfWins: String,
    winsWithoutErrors: String,
    bestWinsLine: String,
    currentWinsLine: String,
    casualGamesPlayed: String,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.game_statistics),
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Card(
            modifier = Modifier.padding(top = AppTheme.paddings.default),
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            Column(modifier = Modifier.padding(vertical = AppTheme.paddings.medium)) {
                StatRow(
                    label = stringResource(R.string.game_started),
                    value = gamesStarted,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.game_won),
                    value = gamesWon,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.percent_of_wins),
                    value = percentOfWins, valueColor = AppTheme.colors.primary,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.wins_no_errors_label),
                    value = winsWithoutErrors,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.best_wins_line),
                    value = bestWinsLine,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.current_wins_line),
                    value = currentWinsLine,
                )

                StatDivider()

                StatRow(
                    label = stringResource(R.string.casual_games),
                    value = casualGamesPlayed,
                )
            }
        }
    }
}
