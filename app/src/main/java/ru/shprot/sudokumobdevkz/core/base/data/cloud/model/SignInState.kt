package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

import android.net.Uri

sealed interface SignInState {

    data object NotAvailable : SignInState

    data object SignedOut : SignInState

    data class SignedIn(
        val playerId: String,
        val displayName: String,
        val avatarUri: Uri?,
    ) : SignInState
}
