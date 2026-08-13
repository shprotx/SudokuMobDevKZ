package ru.shprot.sudokumobdevkz.core.base.data.notification

interface NotificationScheduler {
    fun scheduleDailyReminder(hour: Int, minute: Int)
    fun scheduleReengagement(afterDays: Int)
    fun scheduleGameResumeReminder(delayHours: Int)
    fun cancelAll()
    fun cancel(type: NotificationType)
}
