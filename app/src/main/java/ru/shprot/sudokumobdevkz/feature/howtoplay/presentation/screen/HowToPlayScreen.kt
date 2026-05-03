package ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.components.screencontent.HowToPlayScreenContent
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.contract.HowToPlayUIEffect
import ru.shprot.sudokumobdevkz.feature.howtoplay.presentation.viewmodel.HowToPlayViewModel

@Composable
fun HowToPlayScreen(
    navController: NavController,
    viewModel: HowToPlayViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                HowToPlayUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    HowToPlayScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
