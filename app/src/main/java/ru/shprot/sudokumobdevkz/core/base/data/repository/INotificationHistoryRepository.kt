package ru.shprot.sudokumobdevkz.core.base.data.repository

interface INotificationHistoryRepository {
    suspend fun remainingCapSlots(today: String): Int
    suspend fun tryConsumeCapSlot(today: String): Boolean
    suspend fun reengagementConsecutiveCount(): Int
    suspend fun recordReengagementSent(consecutiveCount: Int)
    suspend fun resetReengagementConsecutiveCount()
    suspend fun lastGameResumeNotifiedTimestamp(): Long?
    suspend fun recordGameResumeNotified(timestamp: Long)
}
