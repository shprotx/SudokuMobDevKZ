package ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.components.screencontent.LeaderboardsScreenContent
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.contract.LeaderboardsUIEffect
import ru.shprot.sudokumobdevkz.feature.leaderboards.presentation.viewmodel.LeaderboardsViewModel
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes

@Composable
fun LeaderboardsScreen(
    navController: NavController,
    viewModel: LeaderboardsViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                LeaderboardsUIEffect.NavigateBack ->
                    navController.popBackStack()

                LeaderboardsUIEffect.NavigateToSettings ->
                    navController.navigate(SettingsRoutes.SettingsScreen)
            }
        }
    }

    LeaderboardsScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
