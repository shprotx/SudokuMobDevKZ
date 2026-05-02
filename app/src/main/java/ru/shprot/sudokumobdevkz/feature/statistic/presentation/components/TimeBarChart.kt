package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun TimeChartSection(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Динамика времени",
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Card(
            modifier = Modifier.padding(top = AppTheme.paddings.default),
            shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
            colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(AppTheme.paddings.large),
                contentAlignment = Alignment.Center,
            ) {
                TimeBarChart(
                    values = listOf(3.5f, 2.8f, 4.2f, 3.0f, 5.1f, 4.3f, 2.5f),
                    labels = listOf("16/05", "17/05", "18/05", "19/05", "20/05", "21/05", "22/05"),
                )
            }
        }
    }
}

@Composable
fun TimeBarChart(
    modifier: Modifier = Modifier,
    values: List<Float>,
    labels: List<String>,
) {
    val barColor = AppTheme.colors.barChart
    val labelColor = AppTheme.colors.barChartLabel
    val labelStyle = AppTheme.typography.caption2

    if (values.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Нет данных",
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }
        return
    }

    val maxValue = values.max()

    Column(modifier = modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            val barCount = values.size
            val totalSpacing = size.width * 0.3f
            val barWidth = (size.width - totalSpacing) / barCount
            val spacing = totalSpacing / (barCount + 1)

            values.forEachIndexed { index, value ->
                val barHeight = if (maxValue > 0f)
                    (value / maxValue) * size.height * 0.85f
                else 0f
                val x = spacing + index * (barWidth + spacing)
                val y = size.height - barHeight

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(barWidth / 4, barWidth / 4),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.small),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            labels.forEach { label ->
                Text(
                    text = label,
                    style = labelStyle,
                    color = labelColor,
                )
            }
        }
    }
}
