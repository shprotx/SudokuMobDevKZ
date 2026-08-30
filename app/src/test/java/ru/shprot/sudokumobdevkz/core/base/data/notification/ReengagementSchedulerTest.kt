package ru.shprot.sudokumobdevkz.core.base.data.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.database.dao.SavedGameDao
import ru.shprot.sudokumobdevkz.core.base.data.database.entity.SavedGameEntity
import ru.shprot.sudokumobdevkz.core.base.data.repository.INotificationHistoryRepository
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import java.time.LocalDate

class ReengagementSchedulerTest {

    private lateinit var notificationScheduler: FakeNotificationScheduler
    private lateinit var settingsRepository: FakeReengagementSettingsRepository
    private lateinit var savedGameDao: FakeSavedGameDao
    private lateinit var historyRepository: FakeNotificationHistoryRepository
    private lateinit var clock: FakeNotificationClock
    private lateinit var scheduler: ReengagementScheduler

    @Before
    fun setUp() {
        notificationScheduler = FakeNotificationScheduler()
        settingsRepository = FakeReengagementSettingsRepository()
        savedGameDao = FakeSavedGameDao()
        historyRepository = FakeNotificationHistoryRepository()
        clock = FakeNotificationClock()
        scheduler = ReengagementScheduler(
            notificationScheduler = notificationScheduler,
            settingsRepository = settingsRepository,
            savedGameDao = savedGameDao,
            notificationHistoryRepository = historyRepository,
            clock = clock,
        )
    }

    @Test
    fun `cancels everything and schedules nothing when notifications are disabled`() = runTest {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.rescheduleAll()

        assertTrue(notificationScheduler.cancelAllCalled)
        assertTrue(notificationScheduler.reengagementCalls.isEmpty())
        assertTrue(notificationScheduler.dailyReminderCalls.isEmpty())
        assertTrue(notificationScheduler.gameResumeCalls.isEmpty())
    }

    @Test
    fun `schedules reengagement and daily reminder when there is no saved game`() = runTest {
        scheduler.rescheduleAll()

        assertEquals(1, notificationScheduler.reengagementCalls.size)
        assertEquals(1, notificationScheduler.dailyReminderCalls.size)
        assertTrue(notificationScheduler.gameResumeCalls.isEmpty())
    }

    @Test
    fun `resets the reengagement consecutive count on every reschedule`() = runTest {
        historyRepository.consecutiveCount = 2

        scheduler.rescheduleAll()

        assertEquals(0, historyRepository.consecutiveCount)
    }

    @Test
    fun `schedules game resume with remaining hours when a saved game exists`() = runTest {
        clock.fixedNowMillis = 10_000_000L
        savedGameDao.saved = SavedGameEntity(timestamp = clock.fixedNowMillis - 3_600_000L)

        scheduler.rescheduleAll()

        assertEquals(1, notificationScheduler.gameResumeCalls.size)
        assertEquals(23, notificationScheduler.gameResumeCalls.first())
    }

    @Test
    fun `does not schedule game resume when that exact saved game was already notified`() = runTest {
        val timestamp = 5_000L
        savedGameDao.saved = SavedGameEntity(timestamp = timestamp)
        historyRepository.lastGameResumeNotified = timestamp

        scheduler.rescheduleAll()

        assertTrue(notificationScheduler.gameResumeCalls.isEmpty())
    }

    @Test
    fun `schedules game resume again once a newer saved game replaces the old one`() = runTest {
        clock.fixedNowMillis = 10_000_000L
        savedGameDao.saved = SavedGameEntity(timestamp = clock.fixedNowMillis)
        historyRepository.lastGameResumeNotified = 1_000L

        scheduler.rescheduleAll()

        assertEquals(1, notificationScheduler.gameResumeCalls.size)
    }

    @Test
    fun `rescheduleAll uses the explicit enabled flag instead of the possibly stale repository value`() = runTest {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.rescheduleAll(notificationsEnabled = true)

        assertEquals(1, notificationScheduler.reengagementCalls.size)
        assertEquals(1, notificationScheduler.dailyReminderCalls.size)
    }

    @Test
    fun `rescheduleAll with an explicit false flag cancels and schedules nothing`() = runTest {
        scheduler.rescheduleAll(notificationsEnabled = false)

        assertTrue(notificationScheduler.cancelAllCalled)
        assertTrue(notificationScheduler.reengagementCalls.isEmpty())
        assertTrue(notificationScheduler.dailyReminderCalls.isEmpty())
    }

    @Test
    fun `scheduleGameResumeAfterSave schedules a fresh reminder when notifications are enabled`() = runTest {
        scheduler.scheduleGameResumeAfterSave()

        assertEquals(1, notificationScheduler.gameResumeCalls.size)
        assertEquals(24, notificationScheduler.gameResumeCalls.first())
    }

    @Test
    fun `scheduleGameResumeAfterSave does nothing when notifications are disabled`() = runTest {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.scheduleGameResumeAfterSave()

        assertTrue(notificationScheduler.gameResumeCalls.isEmpty())
    }
}

private class FakeNotificationScheduler : NotificationScheduler {
    var cancelAllCalled = false
    val reengagementCalls = mutableListOf<Triple<Int, Int, Int>>()
    val dailyReminderCalls = mutableListOf<Pair<Int, Int>>()
    val gameResumeCalls = mutableListOf<Int>()

    override fun scheduleDailyReminder(hour: Int, minute: Int) {
        dailyReminderCalls += hour to minute
    }

    override fun scheduleReengagement(afterDays: Int, hour: Int, minute: Int) {
        reengagementCalls += Triple(afterDays, hour, minute)
    }

    override fun scheduleGameResumeReminder(delayHours: Int) {
        gameResumeCalls += delayHours
    }

    override fun cancelAll() {
        cancelAllCalled = true
    }

    override fun cancel(type: NotificationType) = Unit
}

private class FakeReengagementSettingsRepository : ISettingsRepository {
    private var _settings = AppSettings()
    private val flow = MutableStateFlow(_settings)
    override val settings: Flow<AppSettings> = flow
    override val currentSettings: AppSettings get() = _settings

    override fun update(transform: AppSettings.() -> AppSettings) {
        _settings = _settings.transform()
        flow.value = _settings
    }

    override fun isLeaderboardNamePromptShown(): Flow<Boolean> = MutableStateFlow(false)

    override fun markLeaderboardNamePromptShown() = Unit
}

private class FakeSavedGameDao : SavedGameDao {
    var saved: SavedGameEntity? = null

    override suspend fun save(game: SavedGameEntity) {
        saved = game
    }

    override suspend fun get(): SavedGameEntity? = saved

    override suspend fun delete() {
        saved = null
    }
}

private class FakeNotificationHistoryRepository : INotificationHistoryRepository {
    var remainingSlots = 2
    var consecutiveCount = 0
    var lastGameResumeNotified: Long? = null

    override suspend fun remainingCapSlots(today: String): Int = remainingSlots

    override suspend fun tryConsumeCapSlot(today: String): Boolean {
        if (remainingSlots <= 0) return false
        remainingSlots -= 1
        return true
    }

    override suspend fun reengagementConsecutiveCount(): Int = consecutiveCount

    override suspend fun recordReengagementSent(consecutiveCount: Int) {
        this.consecutiveCount = consecutiveCount
    }

    override suspend fun resetReengagementConsecutiveCount() {
        consecutiveCount = 0
    }

    override suspend fun lastGameResumeNotifiedTimestamp(): Long? = lastGameResumeNotified

    override suspend fun recordGameResumeNotified(timestamp: Long) {
        lastGameResumeNotified = timestamp
    }
}

private class FakeNotificationClock : NotificationClock {
    var fixedToday: LocalDate = LocalDate.of(2026, 8, 13)
    var fixedNowMillis: Long = 0L

    override fun today(): LocalDate = fixedToday

    override fun nowMillis(): Long = fixedNowMillis
}
