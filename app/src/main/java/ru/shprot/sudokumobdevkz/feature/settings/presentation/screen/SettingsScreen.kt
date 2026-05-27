package ru.shprot.sudokumobdevkz.feature.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.flow.collectLatest
import ru.shprot.sudokumobdevkz.BuildConfig
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.core.base.presentation.util.PlayStoreLauncher
import ru.shprot.sudokumobdevkz.core.base.presentation.util.ShareLauncher
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
    val context = LocalContext.current
    val shareText = stringResource(R.string.share_app_text)
    val shareSubject = stringResource(R.string.share_app_subject)
    val shareChooserTitle = stringResource(R.string.share_app_chooser_title)

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                SettingsUIEffect.NavigateBack ->
                    navController.popBackStack()

                SettingsUIEffect.NavigateToPrivacyPolicy ->
                    navController.navigate(SettingsRoutes.PrivacyPolicyScreen)

                SettingsUIEffect.ShareApp ->
                    ShareLauncher.launch(
                        context = context,
                        text = shareText,
                        subject = shareSubject,
                        chooserTitle = shareChooserTitle,
                    )

                SettingsUIEffect.OpenPlayStore ->
                    PlayStoreLauncher.openPlayStore(
                        context = context,
                        packageName = BuildConfig.APPLICATION_ID,
                    )

                SettingsUIEffect.OpenPlayGamesApp -> {
                    val playGamesIntent = context.packageManager
                        .getLaunchIntentForPackage("com.google.android.play.games")
                    if (playGamesIntent != null) {
                        context.startActivity(playGamesIntent)
                    } else {
                        PlayStoreLauncher.openPlayStore(
                            context = context,
                            packageName = "com.google.android.play.games",
                        )
                    }
                }

                is SettingsUIEffect.ShowMessage ->
                    SnackbarManager.show(effect.messageRes)
            }
        }
    }

    SettingsScreenContent(
        uiState = state,
        onEvent = viewModel::setEvent,
    )
}
