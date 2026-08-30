package ru.shprot.sudokumobdevkz.core.base.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationChannelInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun createChannels() {
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        NotificationType.entries.forEach { type ->
            val channel = NotificationChannel(
                type.channelId,
                context.getString(type.channelNameRes),
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply { description = context.getString(type.channelDescriptionRes) }
            manager.createNotificationChannel(channel)
        }
    }
}
