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
import ru.shprot.sudokumobdevkz.core.base.domain.model.Difficulty
import javax.inject.Inject

class AppNotificationFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun build(type: NotificationType, variant: NotificationContentVariant): Notification {
        val title = context.getString(type.titleRes)
        val text = resolveText(variant)
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

    private fun resolveText(variant: NotificationContentVariant): String = when (variant) {
        is NotificationContentVariant.Reengagement -> reengagementText(variant)
        is NotificationContentVariant.DailyReminder -> dailyReminderText(variant)
        is NotificationContentVariant.GameResume -> gameResumeText(variant)
    }

    private fun reengagementText(variant: NotificationContentVariant.Reengagement): String {
        val templates = if (variant.streak > 0) {
            context.resources.getStringArray(R.array.notification_reengagement_texts_streak)
        } else {
            context.resources.getStringArray(R.array.notification_reengagement_texts_default)
        }
        val template = templates[variant.dayIndex.mod(templates.size)]
        return if (variant.streak > 0) String.format(template, variant.streak) else template
    }

    private fun dailyReminderText(variant: NotificationContentVariant.DailyReminder): String =
        if (variant.streak > 0) {
            context.getString(R.string.notification_daily_challenge_text_streak, variant.streak)
        } else {
            context.getString(R.string.notification_daily_challenge_text)
        }

    private fun gameResumeText(variant: NotificationContentVariant.GameResume): String {
        val difficulty = Difficulty.fromOrdinal(variant.difficultyOrdinal)
        return context.getString(R.string.notification_game_resume_text, context.getString(difficulty.titleRes))
    }

    private fun contentIntentFor(type: NotificationType): PendingIntent {
        val intent = Intent(context, ComposeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(NotificationType.EXTRA_NOTIFICATION_TYPE, type.name)
        }
        return PendingIntent.getActivity(
            context,
            type.notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
