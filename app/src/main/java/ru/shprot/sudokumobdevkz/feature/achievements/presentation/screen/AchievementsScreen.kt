package ru.shprot.sudokumobdevkz.feature.achievements.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.components.screencontent.AchievementsScreenContent
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.contract.AchievementsUIEffect
import ru.shprot.sudokumobdevkz.feature.achievements.presentation.viewmodel.AchievementsViewModel

@Composable
fun AchievementsScreen(
    navController: NavHostController,
    viewModel: AchievementsViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                AchievementsUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    AchievementsScreenContent(
        modifier = Modifier,
        uiState = uiState,
        onEvent = viewModel::setEvent,
    )
}