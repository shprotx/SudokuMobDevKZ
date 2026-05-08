package ru.shprot.sudokumobdevkz.feature.menu.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.core.base.data.review.InAppReviewLauncher
import ru.shprot.sudokumobdevkz.core.base.presentation.util.findActivity
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.navigation.AchievementsRoutes
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.navigation.DailyChallengeRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.navigation.HowToPlayRoutes
import ru.shprot.sudokumobdevkz.feature.menu.presentation.components.screencontent.MenuScreenContent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEffect
import ru.shprot.sudokumobdevkz.feature.menu.presentation.contract.MenuUIEvent
import ru.shprot.sudokumobdevkz.feature.menu.presentation.viewmodel.MenuViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.navigation.StatisticRoutes

@Composable
fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.setEvent(MenuUIEvent.ScreenResumed)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                MenuUIEffect.NavigateToContinueGame ->
                    navController.navigate(GameRoutes.GameScreen(continueGame = true))

                MenuUIEffect.NavigateToStatistic ->
                    navController.navigate(StatisticRoutes.StatisticScreen)

                MenuUIEffect.NavigateToAchievements ->
                    navController.navigate(AchievementsRoutes.AchievementsScreen)

                MenuUIEffect.NavigateToSettings ->
                    navController.navigate(SettingsRoutes.SettingsScreen)

                MenuUIEffect.NavigateToHowToPlay ->
                    navController.navigate(HowToPlayRoutes.HowToPlayScreen)

                MenuUIEffect.NavigateToDailyChallenge ->
                    navController.navigate(DailyChallengeRoutes.DailyChallengeScreen)

                is MenuUIEffect.NavigateToGame ->
                    navController.navigate(GameRoutes.GameScreen(difficultyOrdinal = effect.difficultyOrdinal))

                MenuUIEffect.RequestInAppReview ->
                    context.findActivity()?.let { InAppReviewLauncher.launch(it) }
            }
        }
    }

    MenuScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}