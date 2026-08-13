package ru.shprot.sudokumobdevkz.core.base.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.shprot.sudokumobdevkz.core.base.domain.model.AppSettings

class SettingsRepositoryNotificationsTest {

    private lateinit var repository: FakeSettingsRepository

    @Before
    fun setUp() {
        repository = FakeSettingsRepository()
    }

    @Test
    fun `notifications are enabled by default`() {
        assertTrue(repository.currentSettings.notificationsEnabled)
    }

    @Test
    fun `setNotificationsEnabled false disables notifications`() {
        repository.setNotificationsEnabled(false)

        assertFalse(repository.currentSettings.notificationsEnabled)
    }

    @Test
    fun `setNotificationsEnabled true re-enables notifications`() {
        repository.setNotificationsEnabled(false)
        repository.setNotificationsEnabled(true)

        assertTrue(repository.currentSettings.notificationsEnabled)
    }

    @Test
    fun `setNotificationsEnabled does not touch other settings fields`() {
        repository.update { copy(soundsEnabled = false) }

        repository.setNotificationsEnabled(false)

        assertFalse(repository.currentSettings.soundsEnabled)
        assertFalse(repository.currentSettings.notificationsEnabled)
    }

    @Test
    fun `setNotificationsEnabled is observable through the settings flow`() {
        repository.setNotificationsEnabled(false)

        assertEquals(repository.currentSettings, repository.settingsFlow.value)
    }
}

private class FakeSettingsRepository : ISettingsRepository {
    private var _settings = AppSettings()
    val settingsFlow = MutableStateFlow(_settings)
    override val settings: Flow<AppSettings> = settingsFlow
    override val currentSettings: AppSettings get() = _settings

    override fun update(transform: AppSettings.() -> AppSettings) {
        _settings = _settings.transform()
        settingsFlow.value = _settings
    }

    override fun isLeaderboardNamePromptShown(): Flow<Boolean> = MutableStateFlow(false)

    override fun markLeaderboardNamePromptShown() = Unit
}
