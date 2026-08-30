package ru.shprot.sudokumobdevkz.core.base.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import ru.shprot.sudokumobdevkz.core.base.domain.notification.NotificationRateLimiter
import javax.inject.Inject
import javax.inject.Singleton

private val Context.notificationHistoryDataStore by preferencesDataStore(name = "sudoku_notification_history")

@Singleton
class NotificationHistoryRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : INotificationHistoryRepository {

    override suspend fun remainingCapSlots(today: String): Int {
        val prefs = context.notificationHistoryDataStore.data.first()
        return NotificationRateLimiter.remainingSlots(
            today = today,
            lastSlotDate = prefs[Keys.CAP_DATE],
            slotsUsedOnLastDate = prefs[Keys.CAP_COUNT] ?: 0,
        )
    }

    override suspend fun tryConsumeCapSlot(today: String): Boolean {
        var consumed = false
        context.notificationHistoryDataStore.edit { prefs ->
            val remaining = NotificationRateLimiter.remainingSlots(
                today = today,
                lastSlotDate = prefs[Keys.CAP_DATE],
                slotsUsedOnLastDate = prefs[Keys.CAP_COUNT] ?: 0,
            )
            consumed = remaining > 0
            if (consumed) {
                val isSameDay = prefs[Keys.CAP_DATE] == today
                prefs[Keys.CAP_DATE] = today
                prefs[Keys.CAP_COUNT] = if (isSameDay) (prefs[Keys.CAP_COUNT] ?: 0) + 1 else 1
            }
        }
        return consumed
    }

    override suspend fun reengagementConsecutiveCount(): Int =
        context.notificationHistoryDataStore.data.first()[Keys.REENGAGEMENT_CONSECUTIVE] ?: 0

    override suspend fun recordReengagementSent(consecutiveCount: Int) {
        context.notificationHistoryDataStore.edit { prefs ->
            prefs[Keys.REENGAGEMENT_CONSECUTIVE] = consecutiveCount
        }
    }

    override suspend fun resetReengagementConsecutiveCount() {
        context.notificationHistoryDataStore.edit { prefs ->
            prefs[Keys.REENGAGEMENT_CONSECUTIVE] = 0
        }
    }

    override suspend fun lastGameResumeNotifiedTimestamp(): Long? =
        context.notificationHistoryDataStore.data.first()[Keys.GAME_RESUME_NOTIFIED_TIMESTAMP]

    override suspend fun recordGameResumeNotified(timestamp: Long) {
        context.notificationHistoryDataStore.edit { prefs ->
            prefs[Keys.GAME_RESUME_NOTIFIED_TIMESTAMP] = timestamp
        }
    }

    private object Keys {
        val CAP_DATE = stringPreferencesKey("cap_date")
        val CAP_COUNT = intPreferencesKey("cap_count")
        val REENGAGEMENT_CONSECUTIVE = intPreferencesKey("reengagement_consecutive")
        val GAME_RESUME_NOTIFIED_TIMESTAMP = longPreferencesKey("game_resume_notified_timestamp")
    }
}
