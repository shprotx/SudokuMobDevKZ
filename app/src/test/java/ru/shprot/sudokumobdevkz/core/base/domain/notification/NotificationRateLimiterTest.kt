package ru.shprot.sudokumobdevkz.core.base.domain.notification

import org.junit.Assert.assertEquals
import org.junit.Test

class NotificationRateLimiterTest {

    @Test
    fun `returns full slots when the stored date is a different day`() {
        val slots = NotificationRateLimiter.remainingSlots(
            today = "2026-08-13",
            lastSlotDate = "2026-08-12",
            slotsUsedOnLastDate = 2,
        )

        assertEquals(2, slots)
    }

    @Test
    fun `returns full slots when nothing was sent yet`() {
        val slots = NotificationRateLimiter.remainingSlots(
            today = "2026-08-13",
            lastSlotDate = null,
            slotsUsedOnLastDate = 0,
        )

        assertEquals(2, slots)
    }

    @Test
    fun `returns one remaining slot after one push sent today`() {
        val slots = NotificationRateLimiter.remainingSlots(
            today = "2026-08-13",
            lastSlotDate = "2026-08-13",
            slotsUsedOnLastDate = 1,
        )

        assertEquals(1, slots)
    }

    @Test
    fun `returns zero remaining slots after the cap is reached`() {
        val slots = NotificationRateLimiter.remainingSlots(
            today = "2026-08-13",
            lastSlotDate = "2026-08-13",
            slotsUsedOnLastDate = 2,
        )

        assertEquals(0, slots)
    }

    @Test
    fun `never returns a negative slot count`() {
        val slots = NotificationRateLimiter.remainingSlots(
            today = "2026-08-13",
            lastSlotDate = "2026-08-13",
            slotsUsedOnLastDate = 5,
        )

        assertEquals(0, slots)
    }
}
