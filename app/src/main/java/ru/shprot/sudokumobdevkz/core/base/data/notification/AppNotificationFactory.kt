package ru.shprot.sudokumobdevkz.core.base.data.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.shprot.sudokumobdevkz.R
import ru.shprot.sudokumobdevkz.activity.ComposeActivity
import javax.inject.Inject

class AppNotificationFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun build(type: NotificationType): Notification {
        val title = context.getString(type.titleRes)
        val text = context.getString(type.textRes)
        return NotificationCompat.Builder(context, type.channelId)
            .setSmallIcon(R.drawable.ic_notification_small)
            .setColor(ContextCompat.getColor(context, R.color.notification_accent))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setAutoCancel(true)
            .setContentIntent(contentIntentFor(type))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

    private fun contentIntentFor(type: NotificationType): PendingIntent {
        val intent = Intent(context, ComposeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context,
            type.notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
