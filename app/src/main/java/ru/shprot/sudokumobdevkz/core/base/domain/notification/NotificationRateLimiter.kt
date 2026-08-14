package ru.shprot.sudokumobdevkz.core.base.domain.notification

object NotificationRateLimiter {

    const val MAX_PER_DAY = 2

    fun remainingSlots(today: String, lastSlotDate: String?, slotsUsedOnLastDate: Int): Int =
        if (lastSlotDate == today) (MAX_PER_DAY - slotsUsedOnLastDate).coerceAtLeast(0) else MAX_PER_DAY
}
