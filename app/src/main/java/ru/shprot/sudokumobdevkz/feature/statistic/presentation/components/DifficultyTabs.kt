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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun DifficultyTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    TabRow(
        selectedTabIndex = selectedTab,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = AppTheme.paddings.large),
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
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = AppTheme.typography.body2,
                        fontWeight = if (selectedTab == index) FontWeight.SemiBold
                        else FontWeight.Normal,
                        color = if (selectedTab == index) AppTheme.colors.primary
                        else AppTheme.colors.textSecondary,
                    )
                },
            )
        }
    }
}
