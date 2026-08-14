package ru.shprot.sudokumobdevkz.core.base.data.notification

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.data.repository.ISettingsRepository
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings

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

        assertTrue(workGateway.dailyReminderCalls.isEmpty())
    }

    @Test
    fun `scheduleDailyReminder enqueues daily reminder when notifications enabled`() {
        scheduler.scheduleDailyReminder(hour = 20, minute = 0)

        assertEquals(1, workGateway.dailyReminderCalls.size)
    }

    @Test
    fun `scheduleReengagement does nothing when notifications disabled`() {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.scheduleReengagement(afterDays = 3, hour = 19, minute = 0)

        assertTrue(workGateway.reengagementCalls.isEmpty())
    }

    @Test
    fun `scheduleReengagement enqueues one-time work roughly N days ahead when notifications enabled`() {
        scheduler.scheduleReengagement(afterDays = 3, hour = 19, minute = 0)

        assertEquals(1, workGateway.reengagementCalls.size)
        val delayMillis = workGateway.reengagementCalls.first()
        val twoDaysMillis = 2L * 24 * 60 * 60 * 1000
        val fourDaysMillis = 4L * 24 * 60 * 60 * 1000
        assertTrue(delayMillis in twoDaysMillis..fourDaysMillis)
    }

    @Test
    fun `scheduleGameResumeReminder enqueues one-time work in hours when notifications enabled`() {
        scheduler.scheduleGameResumeReminder(delayHours = 6)

        assertEquals(1, workGateway.gameResumeCalls.size)
        assertEquals(6L, workGateway.gameResumeCalls.first())
    }

    @Test
    fun `scheduleGameResumeReminder does nothing when notifications disabled`() {
        settingsRepository.setNotificationsEnabled(false)

        scheduler.scheduleGameResumeReminder(delayHours = 6)

        assertTrue(workGateway.gameResumeCalls.isEmpty())
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
    val dailyReminderCalls = mutableListOf<Long>()
    val reengagementCalls = mutableListOf<Long>()
    val gameResumeCalls = mutableListOf<Long>()
    val cancelledUniqueNames = mutableListOf<String>()

    override fun enqueueDailyReminder(initialDelayMillis: Long) {
        dailyReminderCalls += initialDelayMillis
    }

    override fun enqueueReengagement(initialDelayMillis: Long) {
        reengagementCalls += initialDelayMillis
    }

    override fun enqueueGameResume(delayHours: Long) {
        gameResumeCalls += delayHours
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
