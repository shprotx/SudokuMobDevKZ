package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.GameHistoryEntity
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimeChartSection(
    modifier: Modifier = Modifier,
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

@Composable
internal fun TimeBarChart(
    modifier: Modifier = Modifier,
    games: List<GameHistoryEntity>,
) {
    val maxTime = games.maxOf { it.timeSeconds }.coerceAtLeast(60)
    val chartHeight = 160.dp
    val barWidth = 36.dp
    val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
    val timeLabels = generateTimeLabels(maxTime)
    val listState = rememberLazyListState()

    LaunchedEffect(games.size) {
        if (games.isNotEmpty()) listState.animateScrollToItem(games.size - 1)
    }

    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .height(chartHeight),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                timeLabels.reversed().forEach { label ->
                    Text(
                        text = label,
                        style = AppTheme.typography.caption2,
                        color = AppTheme.colors.textSecondary,
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    timeLabels.reversed().forEach { _ ->
                        HorizontalDivider(
                            color = AppTheme.colors.divider.copy(alpha = 0.5f),
                            thickness = 0.5.dp,
                        )
                    }
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(chartHeight),
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    items(games) { game ->
                        val fraction = (game.timeSeconds.toFloat() / maxTime).coerceIn(0f, 1f)
                        val barColor = if (game.isWin) AppTheme.colors.primary else AppTheme.colors.error.copy(alpha = 0.6f)

                        Column(
                            modifier = Modifier.width(barWidth),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = formatTime(game.timeSeconds),
                                style = AppTheme.typography.caption2,
                                fontWeight = FontWeight.SemiBold,
                                color = AppTheme.colors.textSecondary,
                            )

                            Box(
                                modifier = Modifier
                                    .padding(top = AppTheme.paddings.extraSmall)
                                    .width(barWidth)
                                    .fillMaxHeight(fraction)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(barColor),
                            )
                        }
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 40.dp, top = AppTheme.paddings.small),
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.paddings.medium),
            userScrollEnabled = false,
        ) {
            items(games) { game ->
                Text(
                    modifier = Modifier.width(barWidth),
                    text = dateFormat.format(Date(game.timestamp)),
                    style = AppTheme.typography.caption2,
                    color = AppTheme.colors.textSecondary,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}

private fun generateTimeLabels(maxSeconds: Int): List<String> {
    val step = when {
        maxSeconds <= 120 -> 30
        maxSeconds <= 300 -> 60
        maxSeconds <= 600 -> 120
        maxSeconds <= 1800 -> 300
        else -> 600
    }
    val labels = mutableListOf("0:00")
    var current = step
    while (current <= maxSeconds) {
        labels.add(formatTime(current))
        current += step
    }
    if (labels.size < 3) labels.add(formatTime(maxSeconds))
    return labels
}
