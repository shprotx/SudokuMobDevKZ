package ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.components.screencontent.DailyChallengeScreenContent
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.contract.DailyChallengeUIEffect
import ru.shprot.sudokumobdevkz.feature.dailychallenge.presentation.viewmodel.DailyChallengeViewModel
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes

@Composable
fun DailyChallengeScreen(
    navController: NavController,
    viewModel: DailyChallengeViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                DailyChallengeUIEffect.NavigateBack ->
                    navController.popBackStack()

                is DailyChallengeUIEffect.NavigateToGame ->
                    navController.navigate(
                        GameRoutes.GameScreen(
                            difficultyOrdinal = effect.difficultyOrdinal,
                            isDailyChallenge = true,
                            dailyDateKey = effect.dailyDateKey,
                        )
                    )
            }
        }
    }

    DailyChallengeScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}