package ru.shprot.sudokumobdevkz.core.base.data.util

import kotlinx.coroutines.CancellationException

suspend inline fun <T> safeRunCatching(block: suspend () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellation: CancellationException) {
        throw cancellation
    } catch (error: Throwable) {
        Result.failure(error)
    }
}
