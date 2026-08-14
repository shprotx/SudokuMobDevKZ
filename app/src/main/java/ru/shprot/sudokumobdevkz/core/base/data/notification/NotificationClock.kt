package ru.shprot.sudokumobdevkz.core.base.data.notification

import java.time.LocalDate

interface NotificationClock {
    fun today(): LocalDate
    fun nowMillis(): Long
}
