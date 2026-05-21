package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

sealed interface SignInState {

    data object NotAvailable : SignInState

    data object SignedOut : SignInState

    data class SignedIn(
        val playerId: String,
        val displayName: String,
        val avatarUrl: String?,
    ) : SignInState
}
