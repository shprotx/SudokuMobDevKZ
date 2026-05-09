package ru.shprot.sudokumobdevkz.feature.splash.presentation.components.screencontent

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.core.theme.AppTheme
import ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim.SolvedGridAnimation
import ru.shprot.sudokumobdevkz.core.uicommon.sudokuanim.SolvedPuzzleData
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIEvent
import ru.shprot.sudokumobdevkz.feature.splash.presentation.contract.SplashUIState

@Composable
fun SplashScreenContent(
    uiState: SplashUIState,
    onEvent: (SplashUIEvent) -> Unit,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        when (isLandscape) {
            true -> SplashLandscape(uiState = uiState)
            false -> SplashPortrait(uiState = uiState)
        }
    }
}

@Composable
internal fun SplashPortrait(uiState: SplashUIState) {
    Column(
        modifier = Modifier.padding(horizontal = AppTheme.paddings.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = AppTheme.typography.h1,
            color = AppTheme.colors.text,
        )

        Text(
            modifier = Modifier.padding(top = AppTheme.paddings.small),
            text = stringResource(R.string.version_format, ru.shprot.sudokumobdevkz.BuildConfig.VERSION_NAME),
            style = AppTheme.typography.body3,
            color = AppTheme.colors.textSecondary,
        )

        SolvedGridAnimation(
            modifier = Modifier
                .padding(top = AppTheme.paddings.xxl)
                .fillMaxWidth(0.7f)
                .aspectRatio(1f),
            visibleCells = uiState.visibleCells,
            initialFilled = SolvedPuzzleData.INITIAL_FILLED,
            solvedGrid = SolvedPuzzleData.SOLVED_GRID,
        )
    }
}

@Composable
internal fun SplashLandscape(uiState: SplashUIState) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = AppTheme.paddings.xxl),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier.padding(end = AppTheme.paddings.xxxl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = AppTheme.typography.h1,
                color = AppTheme.colors.text,
            )

            Text(
                modifier = Modifier.padding(top = AppTheme.paddings.small),
                text = stringResource(R.string.version_format, ru.shprot.sudokumobdevkz.BuildConfig.VERSION_NAME),
                style = AppTheme.typography.body3,
                color = AppTheme.colors.textSecondary,
            )
        }

        SolvedGridAnimation(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .aspectRatio(1f),
            visibleCells = uiState.visibleCells,
            initialFilled = SolvedPuzzleData.INITIAL_FILLED,
            solvedGrid = SolvedPuzzleData.SOLVED_GRID,
        )
    }
}
