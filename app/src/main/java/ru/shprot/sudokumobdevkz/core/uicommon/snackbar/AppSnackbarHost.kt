package ru.shprot.sudokumobdevkz.core.uicommon.snackbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import ru.shprot.sudokumobdevkz.core.base.presentation.snackbar.SnackbarManager
import ru.shprot.sudokumobdevkz.core.theme.AppTheme

@Composable
fun AppSnackbarHost(modifier: Modifier) {
    val hostState = remember { SnackbarHostState() }
    var isError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SnackbarManager.messages.collect { msg ->
            isError = msg.isError
            hostState.showSnackbar(
                message = context.getString(msg.messageRes),
                withDismissAction = true,
            )
        }
    }

    SnackbarHost(
        modifier = modifier.padding(bottom = AppTheme.paddings.large),
        hostState = hostState,
        snackbar = { data ->
            Snackbar(
                snackbarData = data,
                containerColor = if (isError) AppTheme.colors.error else AppTheme.colors.primary,
                contentColor = Color.White,
                actionColor = Color.White,
            )
        },
    )
}
