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
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationSchedule
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationScheduler
import ru.shprot.sudokumobdevkz.core.base.data.notification.NotificationType
import ru.shprot.sudokumobdevkz.core.base.data.repository.INotificationHistoryRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.IVisitStreakRepository
import ru.shprot.sudokumobdevkz.core.base.domain.notification.ReengagementRules
import java.time.LocalDate

@HiltWorker
class ReengagementWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val visitStreakRepository: IVisitStreakRepository,
    private val notificationHistoryRepository: INotificationHistoryRepository,
    private val notificationScheduler: NotificationScheduler,
    private val notificationFactory: AppNotificationFactory,
    private val clock: NotificationClock,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.currentSettings.notificationsEnabled) return Result.success()

        val today = clock.today()
        val streak = visitStreakRepository.currentStreak()
        val lastVisitDate = streak.lastVisitDate?.let(LocalDate::parse)
        val consecutiveCount = notificationHistoryRepository.reengagementConsecutiveCount()
        val remainingCapSlots = notificationHistoryRepository.remainingCapSlots(today.toString())

        val decision = ReengagementRules.evaluate(
            today = today,
            lastVisitDate = lastVisitDate,
            currentStreak = streak.currentStreak,
            consecutiveSentCount = consecutiveCount,
            remainingCapSlots = remainingCapSlots,
        )

        when (decision) {
            is ReengagementRules.Decision.Send -> {
                notificationHistoryRepository.consumeCapSlot(today.toString())
                notificationHistoryRepository.recordReengagementSent(decision.consecutiveCount)
                showNotification(decision.streak, decision.consecutiveCount)
                decision.rescheduleAfterDays?.let { afterDays -> rescheduleReengagement(afterDays) }
            }

            is ReengagementRules.Decision.Postpone ->
                rescheduleReengagement(decision.afterDays)

            ReengagementRules.Decision.Stop -> Unit
        }

        return Result.success()
    }

    private fun rescheduleReengagement(afterDays: Int) {
        notificationScheduler.scheduleReengagement(
            afterDays = afterDays,
            hour = NotificationSchedule.REENGAGEMENT_HOUR,
            minute = NotificationSchedule.REENGAGEMENT_MINUTE,
        )
    }

    private fun showNotification(streak: Int, consecutiveCount: Int) {
        try {
            val variant = NotificationContentVariant.Reengagement(streak = streak, dayIndex = consecutiveCount)
            val notification = notificationFactory.build(NotificationType.REENGAGEMENT, variant)
            NotificationManagerCompat.from(applicationContext)
                .notify(NotificationType.REENGAGEMENT.notificationId, notification)
        } catch (_: SecurityException) {
        }
    }
}
