package ru.shprot.sudokumobdevkz.core.base.data.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository

@HiltWorker
class ReminderNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val settingsRepository: ISettingsRepository,
    private val notificationFactory: AppNotificationFactory,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (!settingsRepository.settings.first().notificationsEnabled) return Result.success()

        val type = inputData.getString(KEY_NOTIFICATION_TYPE)
            ?.let { runCatching { NotificationType.valueOf(it) }.getOrNull() }
            ?: return Result.failure()

        return try {
            val notification = notificationFactory.build(type)
            NotificationManagerCompat.from(applicationContext).notify(type.notificationId, notification)
            Result.success()
        } catch (exception: SecurityException) {
            Result.success()
        }
    }

    companion object {
        const val KEY_NOTIFICATION_TYPE = "key_notification_type"
    }
}
