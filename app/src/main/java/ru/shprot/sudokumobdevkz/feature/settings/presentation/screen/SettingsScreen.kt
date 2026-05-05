package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ru.shprot.sudokumobdevkz.feature.settings.presentation.components.screencontent.SettingsScreenContent
import ru.shprot.sudokumobdevkz.feature.settings.presentation.contract.SettingsUIEffect
import ru.shprot.sudokumobdevkz.feature.settings.presentation.navigation.SettingsRoutes
import ru.shprot.sudokumobdevkz.feature.settings.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SettingsUIEffect.NavigateBack ->
                    navController.popBackStack()

                SettingsUIEffect.NavigateToPrivacyPolicy ->
                    navController.navigate(SettingsRoutes.PrivacyPolicyScreen)
            }
        }
    }

    SettingsScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
