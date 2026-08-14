package ru.shprot.sudokumobdevkz.core.base.data.notification

import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.repository.INotificationHistoryRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ReengagementScheduler @Inject constructor(
    private val notificationScheduler: NotificationScheduler,
    private val settingsRepository: ISettingsRepository,
    private val savedGameDao: SavedGameDao,
    private val notificationHistoryRepository: INotificationHistoryRepository,
    private val clock: NotificationClock,
) {

    suspend fun rescheduleAll() {
        notificationScheduler.cancelAll()
        if (!settingsRepository.currentSettings.notificationsEnabled) return

        notificationHistoryRepository.resetReengagementConsecutiveCount()
        notificationScheduler.scheduleReengagement(
            afterDays = NotificationSchedule.REENGAGEMENT_AFTER_DAYS,
            hour = NotificationSchedule.REENGAGEMENT_HOUR,
            minute = NotificationSchedule.REENGAGEMENT_MINUTE,
        )
        notificationScheduler.scheduleDailyReminder(
            hour = NotificationSchedule.DAILY_REMINDER_HOUR,
            minute = NotificationSchedule.DAILY_REMINDER_MINUTE,
        )
        scheduleGameResumeIfNeeded()
    }

    private suspend fun scheduleGameResumeIfNeeded() {
        val savedGame = savedGameDao.get() ?: return
        val alreadyNotified = notificationHistoryRepository.lastGameResumeNotifiedTimestamp()
        if (savedGame.timestamp == alreadyNotified) return

        val elapsedHours = TimeUnit.MILLISECONDS.toHours(clock.nowMillis() - savedGame.timestamp)
        val remainingHours = (NotificationSchedule.GAME_RESUME_AFTER_HOURS - elapsedHours).coerceAtLeast(1L)
        notificationScheduler.scheduleGameResumeReminder(delayHours = remainingHours.toInt())
    }
}
