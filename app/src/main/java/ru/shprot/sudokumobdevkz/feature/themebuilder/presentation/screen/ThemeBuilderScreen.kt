package ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.components.screencontent.ThemeBuilderScreenContent
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.contract.ThemeBuilderUIEffect
import ru.shprot.sudokumobdevkz.feature.themebuilder.presentation.viewmodel.ThemeBuilderViewModel

@Composable
fun ThemeBuilderScreen(
    navController: NavController,
    viewModel: ThemeBuilderViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                ThemeBuilderUIEffect.NavigateBack ->
                    navController.popBackStack()

                is ThemeBuilderUIEffect.ShowMessage ->
                    SnackbarManager.show(effect.messageRes)
            }
        }
    }

    ThemeBuilderScreenContent(
        uiState = uiState,
        onEvent = viewModel::setEvent,
    )
}