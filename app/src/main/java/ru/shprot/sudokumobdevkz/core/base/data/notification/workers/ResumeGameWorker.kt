package ru.shprot.sudokumobdevkz.core.base.data.notification.workers

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.notification.AppNotificationFactory
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationClock
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationContentVariant
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationScheduler
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationType
import ru.shprot.sudokumobdevkz.core.base.data.repository.INotificationHistoryRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IVisitStreakRepository
import ru.shprot.sudokumobdevkz.core.base.domain.notification.GameResumeRules

@HiltWorker
class ResumeGameWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val visitStreakRepository: IVisitStreakRepository,
    private val savedGameDao: SavedGameDao,
    private val notificationHistoryRepository: INotificationHistoryRepository,
    private val notificationScheduler: NotificationScheduler,
    private val notificationFactory: AppNotificationFactory,
    private val clock: NotificationClock,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.currentSettings.notificationsEnabled) return Result.success()

        val today = clock.today().toString()
        val savedGame = savedGameDao.get()
        val visitedToday = visitStreakRepository.currentStreak().lastVisitDate == today
        val alreadyNotified = notificationHistoryRepository.lastGameResumeNotifiedTimestamp()
        val remainingCapSlots = notificationHistoryRepository.remainingCapSlots(today)

        val decision = GameResumeRules.evaluate(
            hasSavedGame = savedGame != null,
            savedGameTimestamp = savedGame?.timestamp,
            alreadyNotifiedTimestamp = alreadyNotified,
            difficultyOrdinal = savedGame?.difficulty ?: 0,
            visitedToday = visitedToday,
            remainingCapSlots = remainingCapSlots,
        )

        when (decision) {
            is GameResumeRules.Decision.Send -> {
                notificationHistoryRepository.consumeCapSlot(today)
                savedGame?.timestamp?.let { timestamp -> notificationHistoryRepository.recordGameResumeNotified(timestamp) }
                showNotification(decision.difficultyOrdinal)
            }

            is GameResumeRules.Decision.Postpone ->
                notificationScheduler.scheduleGameResumeReminder(delayHours = decision.afterHours)

            GameResumeRules.Decision.Skip -> Unit
        }

        return Result.success()
    }

    private fun showNotification(difficultyOrdinal: Int) {
        try {
            val variant = NotificationContentVariant.GameResume(difficultyOrdinal = difficultyOrdinal)
            val notification = notificationFactory.build(NotificationType.GAME_RESUME, variant)
            NotificationManagerCompat.from(applicationContext)
                .notify(NotificationType.GAME_RESUME.notificationId, notification)
        } catch (_: SecurityException) {
        }
    }
}
