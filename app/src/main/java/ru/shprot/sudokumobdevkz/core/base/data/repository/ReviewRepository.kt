package ru.shprot.sudokumobdevkz.core.base.data.repository

interface ReviewRepository {
    suspend fun markSessionWon()
    suspend fun clearSessionWon()
    suspend fun wasSessionWon(): Boolean
    suspend fun markReviewRequested()
    suspend fun lastReviewRequestedAt(): Long
}