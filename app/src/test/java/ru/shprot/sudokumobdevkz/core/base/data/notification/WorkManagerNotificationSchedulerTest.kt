package ru.shprot.sudokumobdevkz.core.base.data.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings
import java.util.concurrent.TimeUnit

class WorkManagerNotificationSchedulerTest {

    private lateinit var workGateway: FakeNotificationWorkGateway
    private lateinit var settingsRepository: FakeSettingsRepository
    private lateinit var scheduler: WorkManagerNotificationScheduler

    @Before
    fun setUp() {
        workGateway = FakeNotificationWorkGateway()
        settingsRepository = FakeSettingsRepository()
        scheduler = WorkManagerNotificationScheduler(workGateway, settingsRepository)
    }

    @Test
    fun `scheduleDailyReminder does nothing when notifications disabled`() {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.scheduleDailyReminder(hour = 20, minute = 0)

        assertTrue(workGateway.periodicCalls.isEmpty())
    }

    @Test
    fun `scheduleDailyReminder enqueues periodic work when notifications enabled`() {
        scheduler.scheduleDailyReminder(hour = 20, minute = 0)

        assertEquals(1, workGateway.periodicCalls.size)
        assertEquals(NotificationType.DAILY_CHALLENGE.workTag, workGateway.periodicCalls.first().uniqueName)
        assertEquals(NotificationType.DAILY_CHALLENGE.name, workGateway.periodicCalls.first().notificationType)
    }

    @Test
    fun `scheduleReengagement does nothing when notifications disabled`() {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.scheduleReengagement(afterDays = 3)

        assertTrue(workGateway.oneTimeCalls.isEmpty())
    }

    @Test
    fun `scheduleReengagement enqueues one-time work in days when notifications enabled`() {
        scheduler.scheduleReengagement(afterDays = 3)

        assertEquals(1, workGateway.oneTimeCalls.size)
        val call = workGateway.oneTimeCalls.first()
        assertEquals(NotificationType.REENGAGEMENT.workTag, call.uniqueName)
        assertEquals(3L, call.initialDelay)
        assertEquals(TimeUnit.DAYS, call.timeUnit)
    }

    @Test
    fun `scheduleGameResumeReminder enqueues one-time work in hours when notifications enabled`() {
        scheduler.scheduleGameResumeReminder(delayHours = 6)

        assertEquals(1, workGateway.oneTimeCalls.size)
        val call = workGateway.oneTimeCalls.first()
        assertEquals(NotificationType.GAME_RESUME.workTag, call.uniqueName)
        assertEquals(6L, call.initialDelay)
        assertEquals(TimeUnit.HOURS, call.timeUnit)
    }

    @Test
    fun `cancelAll cancels every notification type unique work`() {
        scheduler.cancelAll()

        assertEquals(
            setOf(
                NotificationType.DAILY_CHALLENGE.workTag,
                NotificationType.REENGAGEMENT.workTag,
                NotificationType.GAME_RESUME.workTag,
            ),
            workGateway.cancelledUniqueNames.toSet(),
        )
    }

    @Test
    fun `cancel cancels only the requested notification type`() {
        scheduler.cancel(NotificationType.GAME_RESUME)

        assertEquals(listOf(NotificationType.GAME_RESUME.workTag), workGateway.cancelledUniqueNames)
    }

    @Test
    fun `cancelAll works even when notifications are disabled`() {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.cancelAll()

        assertEquals(3, workGateway.cancelledUniqueNames.size)
    }
}

private class FakeNotificationWorkGateway : NotificationWorkGateway {
    data class PeriodicCall(val uniqueName: String, val initialDelayMillis: Long, val notificationType: String)
    data class OneTimeCall(val uniqueName: String, val initialDelay: Long, val timeUnit: TimeUnit, val notificationType: String)

    val periodicCalls = mutableListOf<PeriodicCall>()
    val oneTimeCalls = mutableListOf<OneTimeCall>()
    val cancelledUniqueNames = mutableListOf<String>()

    override fun enqueuePeriodicDaily(uniqueName: String, initialDelayMillis: Long, notificationType: String) {
        periodicCalls += PeriodicCall(uniqueName, initialDelayMillis, notificationType)
    }

    override fun enqueueOneTime(uniqueName: String, initialDelay: Long, timeUnit: TimeUnit, notificationType: String) {
        oneTimeCalls += OneTimeCall(uniqueName, initialDelay, timeUnit, notificationType)
    }

    override fun cancelUniqueWork(uniqueName: String) {
        cancelledUniqueNames += uniqueName
    }
}

private class FakeSettingsRepository : ISettingsRepository {
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
