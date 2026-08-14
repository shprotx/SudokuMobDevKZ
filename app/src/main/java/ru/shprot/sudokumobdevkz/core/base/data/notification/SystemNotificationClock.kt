package ru.shprot.sudokumobdevkz.core.base.data.notification

import java.time.LocalDate
import javax.inject.Inject

class SystemNotificationClock @Inject constructor() : NotificationClock {
    override fun today(): LocalDate = LocalDate.now()
    override fun nowMillis(): Long = System.currentTimeMillis()
}
