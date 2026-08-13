package ru.shprot.sudokumobdevkz.core.base.data.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidNotificationWorkGateway @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationWorkGateway {

    private val workManager get() = WorkManager.getInstance(context)

    override fun enqueuePeriodicDaily(uniqueName: String, initialDelayMillis: Long, notificationType: String) {
        val request = PeriodicWorkRequestBuilder<ReminderNotificationWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf(ReminderNotificationWorker.KEY_NOTIFICATION_TYPE to notificationType))
            .addTag(uniqueName)
            .build()
        workManager.enqueueUniquePeriodicWork(uniqueName, ExistingPeriodicWorkPolicy.UPDATE, request)
    }

    override fun enqueueOneTime(uniqueName: String, initialDelay: Long, timeUnit: TimeUnit, notificationType: String) {
        val request = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
            .setInitialDelay(initialDelay, timeUnit)
            .setInputData(workDataOf(ReminderNotificationWorker.KEY_NOTIFICATION_TYPE to notificationType))
            .addTag(uniqueName)
            .build()
        workManager.enqueueUniqueWork(uniqueName, ExistingWorkPolicy.REPLACE, request)
    }

    override fun cancelUniqueWork(uniqueName: String) {
        workManager.cancelUniqueWork(uniqueName)
    }
}
