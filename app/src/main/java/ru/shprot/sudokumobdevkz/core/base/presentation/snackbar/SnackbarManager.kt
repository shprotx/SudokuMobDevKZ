package ru.shprot.sudokumobdevkz.core.base.presentation.snackbar

import androidx.annotation.StringRes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SnackbarManager {

    private val channel = Channel<SnackbarMessage>(Channel.BUFFERED)

    val messages = channel.receiveAsFlow()

    fun show(@StringRes messageRes: Int, isError: Boolean = false) {
        channel.trySend(SnackbarMessage(messageRes, isError))
    }
}
