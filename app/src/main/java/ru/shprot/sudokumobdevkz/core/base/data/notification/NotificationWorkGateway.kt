package ru.shprot.sudokumobdevkz.core.base.data.notification

interface NotificationWorkGateway {
    fun enqueueDailyReminder(initialDelayMillis: Long)
    fun enqueueReengagement(initialDelayMillis: Long)
    fun enqueueGameResume(delayHours: Long)
    fun cancelUniqueWork(uniqueName: String)
}
