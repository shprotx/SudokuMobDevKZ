package ru.shprot.sudokumobdevkz.feature.feedback.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.components.screencontent.FeedbackScreenContent
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.contract.FeedbackUIEffect
import ru.shprot.sudokumobdevkz.feature.feedback.presentation.viewmodel.FeedbackViewModel

@Composable
fun FeedbackScreen(
    navController: NavController,
    viewModel: FeedbackViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FeedbackUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    FeedbackScreenContent(
        uiState = uiState,
        onEvent = viewModel::setEvent,
    )
}