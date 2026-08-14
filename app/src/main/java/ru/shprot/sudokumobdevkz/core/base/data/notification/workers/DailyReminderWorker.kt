package ru.shprot.sudokumobdevkz.core.base.data.notification.workers

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import ru.shprot.sudokumobdevkz.core.base.data.notification.AppNotificationFactory
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationClock
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationContentVariant
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationType
import ru.shprot.sudokumobdevkz.core.base.data.repository.DailyChallengeRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.INotificationHistoryRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IVisitStreakRepository
import ru.shprot.sudokumobdevkz.core.base.domain.notification.DailyReminderRules

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val visitStreakRepository: IVisitStreakRepository,
    private val dailyChallengeRepository: DailyChallengeRepository,
    private val notificationHistoryRepository: INotificationHistoryRepository,
    private val notificationFactory: AppNotificationFactory,
    private val clock: NotificationClock,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.currentSettings.notificationsEnabled) return Result.success()

        val today = clock.today().toString()
        val visitedToday = visitStreakRepository.currentStreak().lastVisitDate == today
        val challenge = dailyChallengeRepository.getTodayChallenge()
        val streak = dailyChallengeRepository.getCurrentStreak()
        val remainingCapSlots = notificationHistoryRepository.remainingCapSlots(today)

        val decision = DailyReminderRules.evaluate(
            visitedToday = visitedToday,
            isDailyChallengeCompleted = challenge.isCompleted,
            currentStreak = streak,
            remainingCapSlots = remainingCapSlots,
        )

        if (decision is DailyReminderRules.Decision.Send && notificationHistoryRepository.tryConsumeCapSlot(today)) {
            showNotification(decision.streak)
        }

        return Result.success()
    }

    private fun showNotification(streak: Int) {
        try {
            val variant = NotificationContentVariant.DailyReminder(streak = streak)
            val notification = notificationFactory.build(NotificationType.DAILY_CHALLENGE, variant)
            NotificationManagerCompat.from(applicationContext)
                .notify(NotificationType.DAILY_CHALLENGE.notificationId, notification)
        } catch (_: SecurityException) {
        }
    }
}
