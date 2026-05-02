package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.DifficultyTabs
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.GameStatisticsSection
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.OverviewCards
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.StatisticToolbar
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.TimeChartSection

@Composable
fun StatisticScreen(onNavigateBack: () -> Unit) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Лёгкая", "Средняя", "Экспертная")

    Scaffold(containerColor = AppTheme.colors.background) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            StatisticToolbar(onBackClick = onNavigateBack)

            DifficultyTabs(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
            )

            Column(modifier = Modifier.padding(horizontal = AppTheme.paddings.large)) {
                Text(
                    modifier = Modifier.padding(top = AppTheme.paddings.extraLarge),
                    text = "Обзор",
                    style = AppTheme.typography.h4,
                    color = AppTheme.colors.text,
                )

                OverviewCards(modifier = Modifier.padding(top = AppTheme.paddings.default))

                GameStatisticsSection(modifier = Modifier.padding(top = AppTheme.paddings.xxl))

                TimeChartSection(modifier = Modifier.padding(top = AppTheme.paddings.xxl))

                ResetButton(modifier = Modifier.padding(top = AppTheme.paddings.xxl))

                BackToMenuButton(
                    modifier = Modifier.padding(
                        top = AppTheme.paddings.default,
                        bottom = AppTheme.paddings.xxxl,
                    ),
                    onClick = onNavigateBack,
                )
            }
        }
    }
}

@Composable
private fun ResetButton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = null,
            tint = AppTheme.colors.textSecondary,
            modifier = Modifier.size(AppTheme.sizes.iconSmall),
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.medium),
            text = "Сбросить статистику",
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )
    }
}

@Composable
private fun BackToMenuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = ButtonDefaults.buttonColors(containerColor = AppTheme.colors.primary),
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
            contentDescription = null,
            tint = AppTheme.colors.textOnPrimary,
            modifier = Modifier.size(AppTheme.sizes.iconMedium),
        )

        Text(
            modifier = Modifier.padding(start = AppTheme.paddings.medium),
            text = "На главную",
            style = AppTheme.typography.button,
            color = AppTheme.colors.textOnPrimary,
        )
    }
}
