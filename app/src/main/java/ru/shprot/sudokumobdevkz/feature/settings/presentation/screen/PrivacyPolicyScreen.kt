package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent.PrivacyPolicyScreenContent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.PrivacyPolicyUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.PrivacyPolicyViewModel

@Composable
fun PrivacyPolicyScreen(
    navController: NavController,
    viewModel: PrivacyPolicyViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                PrivacyPolicyUIEffect.NavigateBack ->
                    navController.popBackStack()
            }
        }
    }

    PrivacyPolicyScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
