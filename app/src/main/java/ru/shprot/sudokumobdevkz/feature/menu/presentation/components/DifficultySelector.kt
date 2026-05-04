package ru.shprot.sudokumobdevkz.feature.menu.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DifficultySelector(
    modifier: Modifier = Modifier,
    selectedDifficulty: Int,
    onDifficultySelected: (Int) -> Unit,
) {
    val titles = listOf(
        stringResource(R.string.difficulty_easy),
        stringResource(R.string.difficulty_middle),
        stringResource(R.string.difficulty_expert),
    )

    val subtitles = listOf(
        stringResource(R.string.for_beginners),
        stringResource(R.string.for_experienced),
        stringResource(R.string.for_experts),
    )

    val dotColors = listOf(
        AppTheme.colors.primary,
        Color(0xFFFF9500),
        Color(0xFFFF3B30),
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppTheme.paddings.large),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Difficulty.entries.forEachIndexed { index, diff ->
                DifficultyCard(
                    title = titles[index],
                    subtitle = subtitles[index],
                    icon = diff.emoji,
                    dotCount = index + 1,
                    dotColor = dotColors[index],
                    isSelected = selectedDifficulty == index,
                    onClick = { onDifficultySelected(index) },
                )
            }
        }
    }
}

@Composable
internal fun DifficultyCard(
    title: String,
    subtitle: String,
    icon: String,
    dotCount: Int,
    dotColor: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (isSelected)
        AppTheme.colors.primaryLight
    else
        AppTheme.colors.backgroundCard

    Card(
        modifier = Modifier
            .width(105.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) AppTheme.sizes.elevationMedium else 0.dp,
            pressedElevation = 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.paddings.large,
                    horizontal = AppTheme.paddings.medium,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = icon, style = AppTheme.typography.h2)

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                text = title,
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.text,
            )

            Text(
                text = subtitle,
                style = AppTheme.typography.caption2,
                color = AppTheme.colors.textSecondary,
            )

            Row(
                modifier = Modifier.padding(top = AppTheme.paddings.medium),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(AppTheme.sizes.difficultyDot)
                            .clip(CircleShape)
                            .background(
                                if (index < dotCount) dotColor
                                else AppTheme.colors.divider
                            ),
                    )
                }
            }
        }
    }
}
