package ru.shprot.sudokumobdevkz.feature.game.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.game.presentation.components.screencontent.GameScreenContent
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEffect
import ru.shprot.sudokumobdevkz.feature.game.presentation.contract.GameUIEvent
import ru.shprot.sudokumobdevkz.feature.game.presentation.viewmodel.GameViewModel
import ru.shprot.sudokumobdevkz.feature.gameover.presentation.navigation.GameOverRoutes
import ru.shprot.sudokumobdevkz.feature.game.presentation.navigation.GameRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes

@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                viewModel.setEvent(GameUIEvent.SaveState)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                GameUIEffect.NavigateBack ->
                    navController.popBackStack()

                GameUIEffect.NavigateToSettings ->
                    navController.navigate(SettingsRoutes.SettingsScreen)

                is GameUIEffect.NavigateToGameOver ->
                    navController.navigate(
                        GameOverRoutes.GameOverScreen(
                            isWin = effect.isWin,
                            time = effect.time,
                            errors = effect.errors,
                            difficultyOrdinal = state.difficulty.ordinal,
                        )
                    ) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }

                is GameUIEffect.NavigateToNewGame ->
                    navController.navigate(GameRoutes.GameScreen(difficultyOrdinal = effect.difficultyOrdinal)) {
                        popUpTo<GameRoutes.GameScreen> { inclusive = true }
                    }
            }
        }
    }

    GameScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
