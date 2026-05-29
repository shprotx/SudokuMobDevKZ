package ru.shprot.sudokumobdevkz.core.base.presentation.snackbar

import androidx.annotation.StringRes

data class SnackbarMessage(
    @StringRes val messageRes: Int,
    val isError: Boolean = false,
)
