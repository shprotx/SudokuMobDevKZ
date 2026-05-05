package ru.shprot.sudokumobdevkz.feature.statistic.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.components.screencontent.StatisticScreenContent
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.contract.StatisticUIEffect
import ru.shprot.sudokumobdevkz.feature.statistic.presentation.viewmodel.StatisticViewModel

@Composable
fun StatisticScreen(
    navController: NavController,
    viewModel: StatisticViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                StatisticUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    StatisticScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
