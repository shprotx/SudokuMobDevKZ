package ru.shprot.sudokumobdevkz.feature.statistic.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DifficultyTabs(
    modifier: Modifier,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {

    TabRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large),
        selectedTabIndex = selectedTab,
        containerColor = Color.Transparent,
        contentColor = AppTheme.colors.primary,
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                height = 3.dp,
                color = AppTheme.colors.primary,
            )
        },
        divider = {},
    ) {
        Difficulty.entries.forEachIndexed { index, diff ->
            val label = stringResource(
                when (diff) {
                    Difficulty.EASY -> R.string.difficulty_easy
                    Difficulty.MEDIUM -> R.string.difficulty_middle
                    Difficulty.HARD -> R.string.difficulty_expert
                }
            )
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = label,
                        style = AppTheme.typography.body2,
                        fontWeight = when (selectedTab == index) {
                            true -> FontWeight.SemiBold
                            false -> FontWeight.Normal
                        },
                        color = when (selectedTab == index) {
                            true -> AppTheme.colors.primary
                            false -> AppTheme.colors.textSecondary
                        },
                        maxLines = 1,
                    )
                },
            )
        }
    }
}
