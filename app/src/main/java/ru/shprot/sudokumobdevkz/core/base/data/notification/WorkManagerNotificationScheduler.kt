package ru.shprot.sudokumobdevkz.core.base.data.notification

import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerNotificationScheduler @Inject constructor(
    private val workGateway: NotificationWorkGateway,
    private val settingsRepository: ISettingsRepository,
) : NotificationScheduler {

    override fun scheduleDailyReminder(hour: Int, minute: Int) {
        if (!settingsRepository.currentSettings.notificationsEnabled) return
        workGateway.enqueuePeriodicDaily(
            uniqueName = NotificationType.DAILY_CHALLENGE.workTag,
            initialDelayMillis = initialDelayMillisFor(hour, minute),
            notificationType = NotificationType.DAILY_CHALLENGE.name,
        )
    }

    override fun scheduleReengagement(afterDays: Int) {
        if (!settingsRepository.currentSettings.notificationsEnabled) return
        workGateway.enqueueOneTime(
            uniqueName = NotificationType.REENGAGEMENT.workTag,
            initialDelay = afterDays.toLong(),
            timeUnit = TimeUnit.DAYS,
            notificationType = NotificationType.REENGAGEMENT.name,
        )
    }

    override fun scheduleGameResumeReminder(delayHours: Int) {
        if (!settingsRepository.currentSettings.notificationsEnabled) return
        workGateway.enqueueOneTime(
            uniqueName = NotificationType.GAME_RESUME.workTag,
            initialDelay = delayHours.toLong(),
            timeUnit = TimeUnit.HOURS,
            notificationType = NotificationType.GAME_RESUME.name,
        )
    }

    override fun cancelAll() {
        NotificationType.entries.forEach { type -> workGateway.cancelUniqueWork(type.workTag) }
    }

    override fun cancel(type: NotificationType) {
        workGateway.cancelUniqueWork(type.workTag)
    }

    private fun initialDelayMillisFor(hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}
