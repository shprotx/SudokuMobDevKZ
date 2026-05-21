package ru.shprot.sudokumobdevkz.core.base.data.cloud.model

sealed interface SignInResult {

    data object Success : SignInResult

    data object Cancelled : SignInResult

    data object NotAvailable : SignInResult

    data class Failure(val message: String?) : SignInResult
}
