package ru.shprot.sudokumobdevkz.core.base.data.notification

import androidx.annotation.StringRes
import ru.shprot.sudokumobdevkz.R

enum class NotificationType(
    val channelId: String,
    val notificationId: Int,
    val workTag: String,
    @StringRes val channelNameRes: Int,
    @StringRes val channelDescriptionRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val textRes: Int,
) {
    DAILY_CHALLENGE(
        channelId = "daily_challenge",
        notificationId = 1001,
        workTag = "notification_work_daily_challenge",
        channelNameRes = R.string.notification_channel_daily_challenge_name,
        channelDescriptionRes = R.string.notification_channel_daily_challenge_description,
        titleRes = R.string.notification_daily_challenge_title,
        textRes = R.string.notification_daily_challenge_text,
    ),
    REENGAGEMENT(
        channelId = "reengagement",
        notificationId = 1002,
        workTag = "notification_work_reengagement",
        channelNameRes = R.string.notification_channel_reengagement_name,
        channelDescriptionRes = R.string.notification_channel_reengagement_description,
        titleRes = R.string.notification_reengagement_title,
        textRes = R.string.notification_reengagement_text,
    ),
    GAME_RESUME(
        channelId = "game_resume",
        notificationId = 1003,
        workTag = "notification_work_game_resume",
        channelNameRes = R.string.notification_channel_game_resume_name,
        channelDescriptionRes = R.string.notification_channel_game_resume_description,
        titleRes = R.string.notification_game_resume_title,
        textRes = R.string.notification_game_resume_text,
    ),
}
