package ru.shprot.sudokumobdevkz.core.base.data.notification

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.shprot.sudokumobdevkz.core.base.data.notification.workers.DailyReminderWorker
import ru.shprot.sudokumobdevkz.core.base.data.notification.workers.ReengagementWorker
import ru.shprot.sudokumobdevkz.core.base.data.notification.workers.ResumeGameWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AndroidNotificationWorkGateway @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationWorkGateway {

    private val workManager get() = WorkManager.getInstance(context)

    override fun enqueueDailyReminder(initialDelayMillis: Long) {
        val request = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .addTag(NotificationType.DAILY_CHALLENGE.workTag)
            .build()
        workManager.enqueueUniquePeriodicWork(
            NotificationType.DAILY_CHALLENGE.workTag,
            ExistingPeriodicWorkPolicy.UPDATE,
            request,
        )
    }

    override fun enqueueReengagement(initialDelayMillis: Long) {
        val request = OneTimeWorkRequestBuilder<ReengagementWorker>()
            .setInitialDelay(initialDelayMillis, TimeUnit.MILLISECONDS)
            .addTag(NotificationType.REENGAGEMENT.workTag)
            .build()
        workManager.enqueueUniqueWork(NotificationType.REENGAGEMENT.workTag, ExistingWorkPolicy.REPLACE, request)
    }

    override fun enqueueGameResume(delayHours: Long) {
        val request = OneTimeWorkRequestBuilder<ResumeGameWorker>()
            .setInitialDelay(delayHours, TimeUnit.HOURS)
            .addTag(NotificationType.GAME_RESUME.workTag)
            .build()
        workManager.enqueueUniqueWork(NotificationType.GAME_RESUME.workTag, ExistingWorkPolicy.REPLACE, request)
    }

    override fun cancelUniqueWork(uniqueName: String) {
        workManager.cancelUniqueWork(uniqueName)
    }
}
