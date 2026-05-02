package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun MenuScreen(
    onNavigateToGame: (difficulty: Int) -> Unit,
    onNavigateToStatistic: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToHowToPlay: () -> Unit,
) {
    var selectedDifficulty by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = AppTheme.colors.background,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppTheme.paddings.large),
        ) {
            Spacer(modifier = Modifier.height(AppTheme.paddings.large))

            MenuHeader(onSettingsClick = onNavigateToSettings)

            Spacer(modifier = Modifier.height(AppTheme.paddings.extraLarge))

            DailyChallengeCard()

            Spacer(modifier = Modifier.height(AppTheme.paddings.large))

            NewGameButton(onClick = { onNavigateToGame(selectedDifficulty) })

            Spacer(modifier = Modifier.height(AppTheme.paddings.xxl))

            DifficultySelector(
                selectedDifficulty = selectedDifficulty,
                onDifficultySelected = { selectedDifficulty = it },
            )

            Spacer(modifier = Modifier.height(AppTheme.paddings.xxl))

            MenuNavigationCards(
                onStatisticClick = onNavigateToStatistic,
                onHowToPlayClick = onNavigateToHowToPlay,
                onSettingsClick = onNavigateToSettings,
            )

            Spacer(modifier = Modifier.height(AppTheme.paddings.xxxl))
        }
    }
}

@Composable
private fun MenuHeader(onSettingsClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top,
    ) {
        Column {
            Text(
                text = "Sudoku",
                style = AppTheme.typography.h1,
                color = AppTheme.colors.text,
            )
            Spacer(modifier = Modifier.height(AppTheme.paddings.small))
            Text(
                text = "Тренируй мозг. Расслабься. Наслаждайся \uD83C\uDF3F",
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }
        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Настройки",
                tint = AppTheme.colors.iconTint,
            )
        }
    }
}

@Composable
private fun DailyChallengeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = AppTheme.colors.backgroundCardAccent,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Color(0xFFFF9500),
                modifier = Modifier.size(AppTheme.sizes.iconLarge),
            )
            Spacer(modifier = Modifier.width(AppTheme.paddings.default))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ежедневная задача",
                    style = AppTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.text,
                )
                Text(
                    text = "Новая головоломка каждый день",
                    style = AppTheme.typography.caption1,
                    color = AppTheme.colors.textSecondary,
                )
            }
            Text(
                text = "Прогресс",
                style = AppTheme.typography.caption1,
                color = AppTheme.colors.textSecondary,
            )
            Spacer(modifier = Modifier.width(AppTheme.paddings.small))
            Text(
                text = "0/3",
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                color = AppTheme.colors.primary,
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}

@Composable
private fun NewGameButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusXL),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.primary),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.paddings.xxl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.sizes.iconXL)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.paddings.large))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Новая игра",
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.textOnPrimary,
                )
                Text(
                    text = "Начать новую головоломку",
                    style = AppTheme.typography.body5,
                    color = AppTheme.colors.textOnPrimary.copy(alpha = 0.8f),
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textOnPrimary.copy(alpha = 0.7f),
                modifier = Modifier.size(AppTheme.sizes.iconMedium),
            )
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: Int,
    onDifficultySelected: (Int) -> Unit,
) {
    Column {
        Text(
            text = "Выберите сложность",
            style = AppTheme.typography.h4,
            color = AppTheme.colors.text,
        )
        Spacer(modifier = Modifier.height(AppTheme.paddings.large))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            DifficultyCard(
                title = "Лёгкая",
                subtitle = "Для новичков",
                icon = "\uD83C\uDF3F",
                dotCount = 1,
                dotColor = AppTheme.colors.primary,
                isSelected = selectedDifficulty == 0,
                onClick = { onDifficultySelected(0) },
            )
            DifficultyCard(
                title = "Средняя",
                subtitle = "Для опытных",
                icon = "☀\uFE0F",
                dotCount = 2,
                dotColor = Color(0xFFFF9500),
                isSelected = selectedDifficulty == 1,
                onClick = { onDifficultySelected(1) },
            )
            DifficultyCard(
                title = "Сложная",
                subtitle = "Для экспертов",
                icon = "\uD83D\uDC51",
                dotCount = 3,
                dotColor = Color(0xFFFF3B30),
                isSelected = selectedDifficulty == 2,
                onClick = { onDifficultySelected(2) },
            )
        }
    }
}

@Composable
private fun DifficultyCard(
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
            defaultElevation = if (isSelected) AppTheme.sizes.elevationSmall else 0.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.paddings.large, horizontal = AppTheme.paddings.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = icon, style = AppTheme.typography.h2)
            Spacer(modifier = Modifier.height(AppTheme.paddings.medium))
            Text(
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
            Spacer(modifier = Modifier.height(AppTheme.paddings.medium))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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

@Composable
private fun MenuNavigationCards(
    onStatisticClick: () -> Unit,
    onHowToPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.paddings.default)) {
        MenuNavCard(
            icon = Icons.Filled.BarChart,
            title = "Статистика",
            subtitle = "Смотри свои достижения",
            onClick = onStatisticClick,
        )
        MenuNavCard(
            icon = Icons.AutoMirrored.Filled.MenuBook,
            title = "Как играть",
            subtitle = "Правила и советы",
            onClick = onHowToPlayClick,
        )
        MenuNavCard(
            icon = Icons.Filled.Star,
            title = "Достижения",
            subtitle = "Открывай новые награды",
            onClick = { /* stub */ },
        )
        MenuNavCard(
            icon = Icons.Filled.Settings,
            title = "Настройки",
            subtitle = "Тема, звук и другое",
            onClick = onSettingsClick,
        )
    }
}

@Composable
private fun MenuNavCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppTheme.sizes.cornerRadiusLarge),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.backgroundCard),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.paddings.large),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(AppTheme.sizes.cornerRadiusMedium))
                    .background(AppTheme.colors.primaryLight),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AppTheme.colors.primary,
                    modifier = Modifier.size(AppTheme.sizes.iconMedium),
                )
            }
            Spacer(modifier = Modifier.width(AppTheme.paddings.default))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTheme.typography.body2,
                    fontWeight = FontWeight.SemiBold,
                    color = AppTheme.colors.text,
                )
                Text(
                    text = subtitle,
                    style = AppTheme.typography.caption1,
                    color = AppTheme.colors.textSecondary,
                )
            }
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = AppTheme.colors.textSecondary,
            )
        }
    }
}
