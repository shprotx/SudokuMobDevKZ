package ru.shprot.sudokumobdevkz.core.base.data.notification

import java.util.Calendar
import javax.inject.Inject

class WorkManagerNotificationScheduler @Inject constructor(
    private val workGateway: NotificationWorkGateway,
) : NotificationScheduler {

    override fun scheduleDailyReminder(hour: Int, minute: Int) {
        workGateway.enqueueDailyReminder(initialDelayMillisFor(daysAhead = 0, hour = hour, minute = minute))
    }

    override fun scheduleReengagement(afterDays: Int, hour: Int, minute: Int) {
        workGateway.enqueueReengagement(initialDelayMillisFor(daysAhead = afterDays, hour = hour, minute = minute))
    }

    override fun scheduleGameResumeReminder(delayHours: Int) {
        workGateway.enqueueGameResume(delayHours.toLong())
    }

    override fun cancelAll() {
        NotificationType.entries.forEach { type -> workGateway.cancelUniqueWork(type.workTag) }
    }

    override fun cancel(type: NotificationType) {
        workGateway.cancelUniqueWork(type.workTag)
    }

    private fun initialDelayMillisFor(daysAhead: Int, hour: Int, minute: Int): Long {
        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, daysAhead)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (daysAhead == 0 && before(now)) add(Calendar.DAY_OF_YEAR, 1)
        }
        return maxOf(target.timeInMillis - now.timeInMillis, 0L)
    }
}
