package ru.shprot.sudokumobdevkz.core.base.data.notification

sealed interface NotificationContentVariant {
    data class Reengagement(val streak: Int, val dayIndex: Int) : NotificationContentVariant
    data class DailyReminder(val streak: Int, val dayIndex: Int) : NotificationContentVariant
    data class GameResume(val difficultyOrdinal: Int, val dayIndex: Int) : NotificationContentVariant
}
