package ru.shprot.sudokumobdevkz.core.base.data.notification

import java.util.concurrent.TimeUnit

interface NotificationWorkGateway {
    fun enqueuePeriodicDaily(uniqueName: String, initialDelayMillis: Long, notificationType: String)
    fun enqueueOneTime(uniqueName: String, initialDelay: Long, timeUnit: TimeUnit, notificationType: String)
    fun cancelUniqueWork(uniqueName: String)
}
